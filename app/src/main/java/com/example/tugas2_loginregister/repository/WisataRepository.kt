package com.example.tugas2_loginregister.repository

import android.content.Context
import android.net.Uri
import com.example.tugas2_loginregister.model.WisataActionResponse
import com.example.tugas2_loginregister.model.WisataDetailResponse
import com.example.tugas2_loginregister.model.WisataResponse
import com.example.tugas2_loginregister.network.ApiClient
import com.example.tugas2_loginregister.utils.FotoHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    /**
     * Bila [fotoLokal] terisi, data dikirim sebagai multipart supaya file ikut terunggah.
     * Bila kosong, dikirim sebagai form biasa dengan [foto] sebagai URL atau nama file.
     */
    suspend fun tambahWisata(
        namaWisata: String,
        kategori: String,
        lokasi: String,
        hargaTiket: Int,
        deskripsi: String,
        foto: String,
        fotoLokal: Uri? = null
    ): WisataActionResponse {
        if (fotoLokal != null) {
            val berkas = siapkanBerkasFoto(fotoLokal)

            return ApiClient.panggil {
                ApiClient.layanan(context).tambahWisataDenganFoto(
                    namaWisata = FotoHelper.teks(namaWisata),
                    kategori = FotoHelper.teks(kategori),
                    lokasi = FotoHelper.teks(lokasi),
                    hargaTiket = FotoHelper.teks(hargaTiket.toString()),
                    deskripsi = FotoHelper.teks(deskripsi),
                    fotoFile = berkas
                )
            }
        }

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
        foto: String,
        fotoLokal: Uri? = null
    ): WisataActionResponse {
        if (fotoLokal != null) {
            val berkas = siapkanBerkasFoto(fotoLokal)

            return ApiClient.panggil {
                ApiClient.layanan(context).editWisataDenganFoto(
                    id = FotoHelper.teks(id.toString()),
                    namaWisata = FotoHelper.teks(namaWisata),
                    kategori = FotoHelper.teks(kategori),
                    lokasi = FotoHelper.teks(lokasi),
                    hargaTiket = FotoHelper.teks(hargaTiket.toString()),
                    deskripsi = FotoHelper.teks(deskripsi),
                    fotoFile = berkas
                )
            }
        }

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

    /** Membaca dan memadatkan gambar di luar thread utama karena prosesnya cukup berat. */
    private suspend fun siapkanBerkasFoto(fotoLokal: Uri) =
        withContext(Dispatchers.IO) {
            FotoHelper.jadikanPart(context, fotoLokal)
                ?: throw IllegalStateException("Foto yang dipilih tidak dapat dibaca. Coba pilih foto lain.")
        }
}
