package com.example.tugas2_loginregister.utils

import com.example.tugas2_loginregister.network.GagalKoneksi
import com.example.tugas2_loginregister.network.GagalServer

/**
 * Menerjemahkan kegagalan request API menjadi kalimat yang mudah dipahami pengguna.
 *
 * Dipakai oleh seluruh ViewModel CRUD supaya pesan gagal Tambah, Edit, dan Hapus
 * memakai gaya yang sama, bukan menampilkan pesan mentah dari Exception.
 */
object PesanGagal {

    /**
     * [aksi] diisi kata kerja proses yang sedang berjalan, misalnya "menambahkan data wisata".
     * Hasilnya berupa dua baris: baris pertama proses yang gagal, baris kedua penyebabnya.
     */
    fun dari(aksi: String, e: Exception): String {
        return "Gagal $aksi.\n" + penyebab(e)
    }

    private fun penyebab(e: Exception): String {
        return when (e) {
            is GagalKoneksi -> "Server tidak dapat dihubungi. Periksa koneksi internet Anda."
            is GagalServer -> "Server sedang bermasalah. Silakan coba beberapa saat lagi."
            else -> e.message ?: "Terjadi kesalahan yang tidak diketahui."
        }
    }
}
