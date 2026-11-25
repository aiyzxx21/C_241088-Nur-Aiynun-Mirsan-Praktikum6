package com.veltix.praktikum6

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FilmAdapter
    private lateinit var progress: ProgressBar
    private lateinit var tvStatus: TextView
    private lateinit var btnLoad: Button

    private val films = mutableListOf<Film>()

    private var request: Call<ApiResponse<FilmListData>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = findViewById(R.id.progress)
        tvStatus = findViewById(R.id.tvStatus)
        btnLoad = findViewById(R.id.btnLoad)

        adapter = FilmAdapter(this, films)

        val listView = findViewById<ListView>(R.id.listFilm)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val film = films[position]

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, film.id)
            startActivity(intent)
        }

        btnLoad.setOnClickListener {
            loadFilms()
        }

        loadFilms()
    }

    private fun loadFilms() {
        request?.cancel()

        progress.visibility = View.VISIBLE
        btnLoad.isEnabled = false
        tvStatus.text = "Memuat data film..."

        val apiCall = ApiClient.filmApi.getFilms(LIMIT, 0)
        request = apiCall

        apiCall.enqueue(object : Callback<ApiResponse<FilmListData>> {

            override fun onResponse(
                call: Call<ApiResponse<FilmListData>>,
                response: Response<ApiResponse<FilmListData>>
            ) {
                progress.visibility = View.GONE
                btnLoad.isEnabled = true

                val payload = response.body()?.data

                if (response.isSuccessful &&
                    response.body()?.success == true &&
                    payload != null
                ) {
                    films.clear()
                    films.addAll(payload.items)
                    adapter.notifyDataSetChanged()

                    tvStatus.text =
                        "Menampilkan ${payload.items.size} dari ${payload.total} film"
                } else {
                    tvStatus.text =
                        "Gagal memuat daftar (kode ${response.code()})"
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<FilmListData>>,
                t: Throwable
            ) {
                if (call.isCanceled) return

                progress.visibility = View.GONE
                btnLoad.isEnabled = true
                tvStatus.text = "Tidak dapat terhubung ke server"
                Log.e("Film", "Gagal memuat daftar", t)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.cancel()
    }

    companion object {
        private const val LIMIT = 20
    }
}
