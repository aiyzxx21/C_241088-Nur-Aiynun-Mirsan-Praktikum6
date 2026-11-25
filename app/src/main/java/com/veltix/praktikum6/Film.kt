package com.veltix.praktikum6

import com.google.gson.annotations.SerializedName

// Amplop respons API: success, message, dan data
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)

// Data untuk respons daftar film
data class FilmListData(
    val items: List<Film>,
    val total: Int,
    val limit: Int,
    val offset: Int
)

// Kategori film
data class Kategori(
    val id: Int,
    val name: String
)

// Satu kelas untuk data daftar dan detail film
data class Film(
    val id: Int,
    val title: String,
    val year: Int?,
    val rating: Double,
    @SerializedName("poster_url") val posterUrl: String?,
    val categories: List<Kategori>,
    val tagline: String?,
    val overview: String?,
    val runtime: Int?
)
