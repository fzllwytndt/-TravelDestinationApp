package com.example.tugas2_loginregister.utils

/**
 * Keadaan tampilan yang dikirim ViewModel ke Activity atau Fragment.
 * Dengan begini tampilan cukup menunggu satu kabar saja: sedang memuat, berhasil, atau gagal.
 */
sealed class UiState<out T> {

    object Loading : UiState<Nothing>()

    data class Berhasil<out T>(val data: T) : UiState<T>()

    /** [penyebab] disimpan supaya dialog alamat server tahu jenis kegagalannya. */
    data class Gagal(val pesan: String, val penyebab: Exception? = null) : UiState<Nothing>()
}
