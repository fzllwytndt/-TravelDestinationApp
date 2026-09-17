package com.example.tugas2_loginregister.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.tugas2_loginregister.data.local.room.FavoriteWisata
import com.example.tugas2_loginregister.data.local.room.WisataDatabase
import com.example.tugas2_loginregister.model.Wisata

/**
 * Penghubung antara ViewModel dengan DAO.
 * ViewModel cukup memanggil Repository, jadi tidak perlu tahu cara kerja Room.
 */
class FavoriteRepository(context: Context) {

    private val dao = WisataDatabase.ambilData(context).favoriteWisataDao()

    fun ambilSemua(): LiveData<List<FavoriteWisata>> {
        return dao.ambilSemua()
    }

    fun cekFavorit(id: Int): LiveData<Boolean> {
        return dao.cekFavorit(id)
    }

    /** Data wisata dari API disalin menjadi data favorit sebelum disimpan ke Room. */
    suspend fun simpan(wisata: Wisata) {
        dao.simpan(
            FavoriteWisata(
                id = wisata.id,
                namaWisata = wisata.namaWisata,
                kategori = wisata.kategori,
                lokasi = wisata.lokasi,
                hargaTiket = wisata.hargaTiket,
                deskripsi = wisata.deskripsi,
                fotoUrl = wisata.fotoUrl
            )
        )
    }

    suspend fun hapus(id: Int) {
        dao.hapus(id)
    }
}
