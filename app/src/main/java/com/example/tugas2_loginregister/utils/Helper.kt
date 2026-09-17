package com.example.tugas2_loginregister.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

/** Kumpulan perintah pendek yang sering dipakai di banyak halaman. */
object Helper {

    fun tampil(vararg daftarView: View) {
        for (view in daftarView) {
            view.visibility = View.VISIBLE
        }
    }

    fun sembunyi(vararg daftarView: View) {
        for (view in daftarView) {
            view.visibility = View.GONE
        }
    }

    fun tampilJika(view: View, syarat: Boolean) {
        view.visibility = if (syarat) View.VISIBLE else View.GONE
    }

    fun muatGambar(gambar: ImageView, url: String) {
        Glide.with(gambar.context)
            .load(url)
            .into(gambar)
    }

    fun pesanSingkat(context: Context, teks: String) {
        Toast.makeText(context, teks, Toast.LENGTH_SHORT).show()
    }

    fun rupiah(harga: Int): String {
        if (harga <= 0) {
            return "Gratis"
        }

        val angka = harga.toString()
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()

        return "Rp $angka"
    }
}
