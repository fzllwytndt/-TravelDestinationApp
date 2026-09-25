package com.example.tugas2_loginregister.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

/**
 * Menyiapkan foto pilihan pengguna supaya siap dikirim ke API.
 *
 * Foto dari galeri ponsel umumnya berukuran 3-8 MB, sedangkan XAMPP secara bawaan
 * hanya menerima unggahan 2 MB. Karena itu gambar diperkecil dan dipadatkan dulu
 * menjadi JPEG sebelum dikirim.
 */
object FotoHelper {

    /** Nama part yang ditunggu oleh wisata_add.php dan wisata_edit.php. */
    const val NAMA_PART = "foto_file"

    private const val SISI_TERPANJANG = 1280
    private const val MUTU_JPEG = 80

    /** Membungkus teks biasa agar bisa ikut dikirim bersama file pada permintaan multipart. */
    fun teks(nilai: String): RequestBody {
        return nilai.toRequestBody("text/plain".toMediaType())
    }

    /**
     * Membaca gambar pada [uri], memperkecil, lalu membungkusnya menjadi part multipart.
     * Mengembalikan null apabila gambar tidak dapat dibaca.
     */
    fun jadikanPart(context: Context, uri: Uri): MultipartBody.Part? {
        val gambar = bacaGambarKecil(context, uri) ?: return null

        val penampung = ByteArrayOutputStream()
        gambar.compress(Bitmap.CompressFormat.JPEG, MUTU_JPEG, penampung)
        gambar.recycle()

        val isi = penampung.toByteArray()
        val badan = isi.toRequestBody("image/jpeg".toMediaType(), 0, isi.size)

        return MultipartBody.Part.createFormData(NAMA_PART, namaBerkas(context, uri), badan)
    }

    /** Membaca ukuran asli lebih dulu supaya gambar besar tidak langsung dimuat penuh ke memori. */
    private fun bacaGambarKecil(context: Context, uri: Uri): Bitmap? {
        val resolver = context.contentResolver

        // Dengan inJustDecodeBounds, decodeStream memang selalu mengembalikan null dan hanya
        // mengisi outWidth serta outHeight. Jadi keberhasilannya diperiksa dari ukuran itu,
        // bukan dari nilai kembalian decodeStream.
        val opsiUkur = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opsiUkur) }

        if (opsiUkur.outWidth <= 0 || opsiUkur.outHeight <= 0) {
            return null
        }

        val opsiBaca = BitmapFactory.Options().apply {
            inSampleSize = hitungSkala(opsiUkur.outWidth, opsiUkur.outHeight)
        }

        return resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opsiBaca) }
    }

    private fun hitungSkala(lebar: Int, tinggi: Int): Int {
        var skala = 1

        while (lebar / (skala * 2) >= SISI_TERPANJANG || tinggi / (skala * 2) >= SISI_TERPANJANG) {
            skala *= 2
        }

        return skala
    }

    /**
     * PHP memakai nama berkas kiriman ini sebagai nama file di folder uploads,
     * jadi karakter aneh dibuang dan akhirannya dipaksa .jpg mengikuti hasil pemadatan.
     */
    private fun namaBerkas(context: Context, uri: Uri): String {
        val namaAsli = context.contentResolver
            .query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
            ?.use { kursor -> if (kursor.moveToFirst()) kursor.getString(0) else null }

        val dasar = (namaAsli ?: "foto_wisata")
            .substringBeforeLast('.')
            .replace(Regex("[^a-zA-Z0-9_-]"), "_")
            .take(40)

        return dasar.ifBlank { "foto_wisata" } + ".jpg"
    }
}
