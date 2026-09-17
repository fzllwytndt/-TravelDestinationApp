package com.example.tugas2_loginregister.repository

import android.content.Context
import com.example.tugas2_loginregister.model.WisataDetailResponse
import com.example.tugas2_loginregister.model.WisataResponse
import com.example.tugas2_loginregister.network.ApiClient

/** Penghubung antara ViewModel dengan API daftar wisata dan detail wisata. */
class WisataRepository(private val context: Context) {

    suspend fun ambilDaftarWisata(halaman: Int, kataKunci: String): WisataResponse {
        return ApiClient.panggil {
            ApiClient.layanan(context).ambilDaftarWisata(halaman, kataKunci)
        }
    }

    suspend fun ambilDetailWisata(id: Int): WisataDetailResponse {
        return ApiClient.panggil {
            ApiClient.layanan(context).ambilDetailWisata(id)
        }
    }
}
