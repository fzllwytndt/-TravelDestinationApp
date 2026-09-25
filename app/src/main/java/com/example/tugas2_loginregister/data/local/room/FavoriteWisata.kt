package com.example.tugas2_loginregister.data.local.room

import androidx.room.Entity

/**
 * Bentuk satu baris data pada tabel favorit.
 * Isinya disalin dari data wisata yang diambil dari API supaya daftar favorit
 * tetap dapat ditampilkan walaupun aplikasi sedang tidak terhubung ke server.
 *
 * Kunci utamanya gabungan [id] dan [username] supaya satu wisata yang sama
 * dapat difavoritkan oleh beberapa akun tanpa saling menimpa, dan daftar favorit
 * setiap akun tetap terpisah.
 */
@Entity(
    tableName = "favorite_wisata",
    primaryKeys = ["id", "username"]
)
data class FavoriteWisata(

    val id: Int,

    /** Pemilik data favorit ini, diisi dari username yang sedang login. */
    val username: String,

    val namaWisata: String,

    val kategori: String,

    val lokasi: String,

    val hargaTiket: Int,

    val deskripsi: String,

    val fotoUrl: String
)
