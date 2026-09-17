package com.example.tugas2_loginregister.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/** Perintah-perintah yang boleh dijalankan pada tabel favorit. */
@Dao
interface FavoriteWisataDao {

    /** Dipakai Fragment Favorit. LiveData membuat daftar ikut berubah sendiri setiap ada perubahan data. */
    @Query("SELECT * FROM favorite_wisata ORDER BY namaWisata ASC")
    fun ambilSemua(): LiveData<List<FavoriteWisata>>

    /** Dipakai halaman Detail untuk menentukan ikon love berwarna merah atau hitam. */
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_wisata WHERE id = :id)")
    fun cekFavorit(id: Int): LiveData<Boolean>

    /** Wisata yang sama cukup tersimpan satu kali, jadi data lama ditimpa. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun simpan(wisata: FavoriteWisata)

    @Query("DELETE FROM favorite_wisata WHERE id = :id")
    suspend fun hapus(id: Int)
}
