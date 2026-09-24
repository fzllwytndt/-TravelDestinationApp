package com.example.tugas2_loginregister.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Perintah-perintah yang boleh dijalankan pada tabel favorit.
 *
 * Hampir semuanya menyertakan [username] supaya setiap akun hanya menyentuh
 * data favoritnya sendiri.
 */
@Dao
interface FavoriteWisataDao {

    /** Dipakai Fragment Favorit. LiveData membuat daftar ikut berubah sendiri setiap ada perubahan data. */
    @Query("SELECT * FROM favorite_wisata WHERE username = :username ORDER BY namaWisata ASC")
    fun ambilSemua(username: String): LiveData<List<FavoriteWisata>>

    /** Dipakai halaman Detail untuk menentukan ikon love berwarna merah atau hitam. */
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_wisata WHERE id = :id AND username = :username)")
    fun cekFavorit(id: Int, username: String): LiveData<Boolean>

    /** Wisata yang sama cukup tersimpan satu kali per akun, jadi data lama ditimpa. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun simpan(wisata: FavoriteWisata)

    /**
     * Daftar akun yang memfavoritkan wisata [id].
     * Dipakai saat wisatanya diubah, supaya salinan favorit milik setiap akun ikut disegarkan.
     */
    @Query("SELECT username FROM favorite_wisata WHERE id = :id")
    suspend fun pemilikFavorit(id: Int): List<String>

    /** Batal favorit oleh satu akun saja. */
    @Query("DELETE FROM favorite_wisata WHERE id = :id AND username = :username")
    suspend fun hapus(id: Int, username: String)

    /** Dipakai ketika wisatanya sudah dihapus dari server, jadi tidak lagi milik siapa pun. */
    @Query("DELETE FROM favorite_wisata WHERE id = :id")
    suspend fun hapusSemuaAkun(id: Int)
}
