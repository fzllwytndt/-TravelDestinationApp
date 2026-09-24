package com.example.tugas2_loginregister.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.tugas2_loginregister.data.local.room.FavoriteWisata
import com.example.tugas2_loginregister.data.local.room.WisataDatabase
import com.example.tugas2_loginregister.model.Wisata
import com.example.tugas2_loginregister.utils.SessionManager

/**
 * Penghubung antara ViewModel dengan DAO.
 * ViewModel cukup memanggil Repository, jadi tidak perlu tahu cara kerja Room.
 *
 * Repository ini juga yang menentukan pemilik data favorit, diambil dari akun
 * yang sedang login. Dengan begitu ViewModel tidak perlu ikut mengurus sesi.
 */
class FavoriteRepository(private val context: Context) {

    private val dao = WisataDatabase.ambilData(context).favoriteWisataDao()

    /** Dibaca setiap kali dipakai, bukan disimpan sekali, supaya selalu mengikuti akun yang sedang login. */
    private fun pemilik(): String {
        return SessionManager.ambilUsername(context)
    }

    fun ambilSemua(): LiveData<List<FavoriteWisata>> {
        return dao.ambilSemua(pemilik())
    }

    fun cekFavorit(id: Int): LiveData<Boolean> {
        return dao.cekFavorit(id, pemilik())
    }

    /** Data wisata dari API disalin menjadi data favorit sebelum disimpan ke Room. */
    suspend fun simpan(wisata: Wisata) {
        dao.simpan(jadikanFavorit(wisata, pemilik()))
    }

    /**
     * Dipanggil setelah wisata diubah supaya daftar favorit tidak menampilkan data lama.
     * Wisata yang sama bisa difavoritkan beberapa akun, jadi seluruh salinannya ikut disegarkan.
     * Bila belum ada yang memfavoritkan, tidak ada yang dikerjakan.
     */
    suspend fun perbarui(wisata: Wisata) {
        for (pemilik in dao.pemilikFavorit(wisata.id)) {
            dao.simpan(jadikanFavorit(wisata, pemilik))
        }
    }

    /** Batal favorit oleh akun yang sedang login. Akun lain tidak terpengaruh. */
    suspend fun hapus(id: Int) {
        dao.hapus(id, pemilik())
    }

    /** Wisatanya sudah dihapus dari server, jadi dibuang dari daftar favorit seluruh akun. */
    suspend fun hapusSemuaAkun(id: Int) {
        dao.hapusSemuaAkun(id)
    }

    private fun jadikanFavorit(wisata: Wisata, username: String) = FavoriteWisata(
        id = wisata.id,
        username = username,
        namaWisata = wisata.namaWisata,
        kategori = wisata.kategori,
        lokasi = wisata.lokasi,
        hargaTiket = wisata.hargaTiket,
        deskripsi = wisata.deskripsi,
        fotoUrl = wisata.fotoUrl
    )
}
