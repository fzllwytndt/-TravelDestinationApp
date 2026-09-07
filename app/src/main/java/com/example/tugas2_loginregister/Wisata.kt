package com.example.tugas2_loginregister

// Menampung satu data wisata yang dikirim oleh API.
// Gambarnya sendiri tidak ikut disimpan di sini, yang disimpan hanya alamatnya
// (fotoUrl), lalu Glide yang mengunduh gambarnya dari server.
data class Wisata(
    val id: Int,
    val namaWisata: String,
    val deskripsi: String,
    val fotoUrl: String
)
