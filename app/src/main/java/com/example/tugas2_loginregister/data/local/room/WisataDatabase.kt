package com.example.tugas2_loginregister.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/** Database lokal tempat data favorit disimpan di dalam perangkat pengguna. */
@Database(
    entities = [FavoriteWisata::class],
    version = 2,
    exportSchema = false
)
abstract class WisataDatabase : RoomDatabase() {

    abstract fun favoriteWisataDao(): FavoriteWisataDao

    companion object {

        private const val NAMA_DATABASE = "wisata.db"

        @Volatile
        private var instance: WisataDatabase? = null

        /**
         * Versi 1 menyimpan favorit tanpa penanda pemilik, sehingga daftarnya ikut terbawa
         * ketika pengguna berganti akun. Versi 2 menambahkan kolom `username` dan memakai
         * kunci utama gabungan `id` + `username`.
         *
         * Baris lama tidak dapat ditentukan milik akun yang mana, jadi tabelnya dibuat ulang
         * dalam keadaan kosong. Favorit cukup ditandai lagi setelah login.
         */
        private val MIGRASI_1_2 = object : Migration(1, 2) {

            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS favorite_wisata")
                db.execSQL(
                    "CREATE TABLE favorite_wisata (" +
                        "id INTEGER NOT NULL, " +
                        "username TEXT NOT NULL, " +
                        "namaWisata TEXT NOT NULL, " +
                        "kategori TEXT NOT NULL, " +
                        "lokasi TEXT NOT NULL, " +
                        "hargaTiket INTEGER NOT NULL, " +
                        "deskripsi TEXT NOT NULL, " +
                        "fotoUrl TEXT NOT NULL, " +
                        "PRIMARY KEY(id, username))"
                )
            }
        }

        /** Database hanya dibuat satu kali, lalu dipakai bersama oleh seluruh halaman. */
        fun ambilData(context: Context): WisataDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    WisataDatabase::class.java,
                    NAMA_DATABASE
                ).addMigrations(MIGRASI_1_2)
                    .build()
                    .also { instance = it }
            }
        }
    }
}
