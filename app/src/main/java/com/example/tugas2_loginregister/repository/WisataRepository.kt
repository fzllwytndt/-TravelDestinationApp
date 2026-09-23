package com.example.tugas2_loginregister.repository

import android.content.Context
import com.example.tugas2_loginregister.model.WisataActionResponse
import com.example.tugas2_loginregister.model.WisataDetailResponse
import com.example.tugas2_loginregister.model.WisataResponse
import com.example.tugas2_loginregister.network.ApiClient

/** Penghubung antara ViewModel dengan API daftar wisata, detail, serta aksi CRUD. */
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

    suspend fun tambahWisata(
        namaWisata: String,
        kategori: String,
        lokasi: String,
        hargaTiket: Int,
        deskripsi: String,
        foto: String
    ): WisataActionResponse {
        return ApiClient.panggil {
            ApiClient.layanan(context).tambahWisata(
                namaWisata = namaWisata,
                kategori = kategori,
                lokasi = lokasi,
                hargaTiket = hargaTiket,
                deskripsi = deskripsi,
                foto = foto
            )
        }
    }

    suspend fun editWisata(
        id: Int,
        namaWisata: String,
        kategori: String,
        lokasi: String,
        hargaTiket: Int,
        deskripsi: String,
        foto: String
    ): WisataActionResponse {
        return ApiClient.panggil {
            ApiClient.layanan(context).editWisata(
                id = id,
                namaWisata = namaWisata,
                kategori = kategori,
                lokasi = lokasi,
                hargaTiket = hargaTiket,
                deskripsi = deskripsi,
                foto = foto
            )
        }
    }

    suspend fun hapusWisata(id: Int): WisataActionResponse {
        return ApiClient.panggil {
            ApiClient.layanan(context).hapusWisata(id)
        }
    }
}
