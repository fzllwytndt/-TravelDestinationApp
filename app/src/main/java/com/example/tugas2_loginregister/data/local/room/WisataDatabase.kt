package com.example.tugas2_loginregister.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** Database lokal tempat data favorit disimpan di dalam perangkat pengguna. */
@Database(
    entities = [FavoriteWisata::class],
    version = 1,
    exportSchema = false
)
abstract class WisataDatabase : RoomDatabase() {

    abstract fun favoriteWisataDao(): FavoriteWisataDao

    companion object {

        private const val NAMA_DATABASE = "wisata.db"

        @Volatile
        private var instance: WisataDatabase? = null

        /** Database hanya dibuat satu kali, lalu dipakai bersama oleh seluruh halaman. */
        fun ambilData(context: Context): WisataDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    WisataDatabase::class.java,
                    NAMA_DATABASE
                ).build().also { instance = it }
            }
        }
    }
}
