package com.veltix.praktikum6

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil3.load
import coil3.request.error
import coil3.request.placeholder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var progress: ProgressBar
    private lateinit var tvStatus: TextView

    private var request: Call<ApiResponse<Film>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        progress = findViewById(R.id.progress)
        tvStatus = findViewById(R.id.tvStatus)

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }

        loadDetail(intent.getIntExtra(EXTRA_ID, -1))
    }

    private fun loadDetail(id: Int) {

        if (id <= 0) {
            progress.visibility = View.GONE
            tvStatus.text = "ID film tidak valid"
            return
        }

        progress.visibility = View.VISIBLE
        tvStatus.text = "Memuat detail film..."

        val apiCall = ApiClient.filmApi.getFilm(id)
        request = apiCall

        apiCall.enqueue(object : Callback<ApiResponse<Film>> {

            override fun onResponse(
                call: Call<ApiResponse<Film>>,
                response: Response<ApiResponse<Film>>
            ) {
                progress.visibility = View.GONE

                val film = response.body()?.data

                if (response.isSuccessful &&
                    response.body()?.success == true &&
                    film != null
                ) {
                    tvStatus.text = ""
                    show(film)

                } else if (response.code() == 404) {
                    tvStatus.text =
                        "Film tidak ditemukan (kode ${response.code()})"
                } else {
                    tvStatus.text =
                        "Gagal memuat detail (kode ${response.code()})"
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<Film>>,
                t: Throwable
            ) {
                if (call.isCanceled) return

                progress.visibility = View.GONE
                tvStatus.text = "Tidak dapat terhubung ke server"

                Log.e("Film", "Gagal memuat detail id=$id", t)
            }
        })
    }

    private fun show(film: Film) {

        val info = mutableListOf<String>()

        film.year?.let { info.add(it.toString()) }

        film.runtime?.let {
            info.add("$it menit")
        }

        if (film.rating > 0) {
            info.add("Rating ${film.rating}")
        }

        findViewById<TextView>(R.id.tvTitle).text = film.title

        findViewById<TextView>(R.id.tvInfo).text =
            info.joinToString(" • ")

        findViewById<TextView>(R.id.tvCategories).text =
            film.categories.joinToString(", ") { it.name }

        findViewById<TextView>(R.id.tvTagline).apply {
            text = film.tagline.orEmpty()
            visibility = if (film.tagline.isNullOrBlank()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        findViewById<TextView>(R.id.tvOverview).text =
            film.overview?.takeIf { it.isNotBlank() }
                ?: "Sinopsis belum tersedia"

        findViewById<ImageView>(R.id.ivPoster).load(film.posterUrl) {
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.ic_menu_gallery)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.cancel()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
