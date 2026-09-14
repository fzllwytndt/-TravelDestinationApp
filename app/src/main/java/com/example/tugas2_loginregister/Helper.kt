package com.example.tugas2_loginregister

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.net.URLEncoder

object Helper {

    fun ambilDataApi(
        activity: Activity,
        alamat: String,
        saatBerhasil: (JSONObject) -> Unit,
        saatGagal: () -> Unit
    ) {
        Thread {
            try {
                val json = JSONObject(ApiClient.get(activity, alamat))

                activity.runOnUiThread {
                    try {
                        saatBerhasil(json)
                    } catch (e: Exception) {
                        saatGagal()
                    }
                }

            } catch (e: Exception) {
                activity.runOnUiThread { saatGagal() }
            }
        }.start()
    }

    fun bacaWisata(json: JSONObject): Wisata {
        return Wisata(
            id = json.getInt("id"),
            namaWisata = json.getString("nama_wisata"),
            kategori = json.getString("kategori"),
            lokasi = json.getString("lokasi"),
            hargaTiket = json.getInt("harga_tiket"),
            deskripsi = json.getString("deskripsi"),
            fotoUrl = json.getString("foto_url")
        )
    }

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

    fun sandikan(teks: String): String {
        return URLEncoder.encode(teks, "UTF-8")
    }
}
