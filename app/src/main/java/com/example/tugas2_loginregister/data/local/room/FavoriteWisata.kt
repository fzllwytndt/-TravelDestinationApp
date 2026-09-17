package com.example.tugas2_loginregister.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Bentuk satu baris data pada tabel favorit.
 * Isinya disalin dari data wisata yang diambil dari API supaya daftar favorit
 * tetap dapat ditampilkan walaupun aplikasi sedang tidak terhubung ke server.
 */
@Entity(tableName = "favorite_wisata")
data class FavoriteWisata(

    @PrimaryKey
    val id: Int,

    val namaWisata: String,

    val kategori: String,

    val lokasi: String,

    val hargaTiket: Int,

    val deskripsi: String,

    val fotoUrl: String
)
