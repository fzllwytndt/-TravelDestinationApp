package com.example.tugas2_loginregister.utils

import android.app.Activity
import android.text.InputType
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.example.tugas2_loginregister.network.ApiClient
import com.example.tugas2_loginregister.network.GagalKoneksi

object DialogServer {

    /**
     * Memilih dialog yang sesuai dengan penyebab kegagalan.
     * Dialog alamat server hanya muncul kalau server memang tidak dapat dihubungi,
     * bukan setiap kali terjadi error.
     */
    fun tangani(activity: Activity, e: Exception, cobaLagi: () -> Unit) {
        if (activity.isFinishing) {
            return
        }

        if (e is GagalKoneksi) {
            tampilkan(activity, e.message ?: "", cobaLagi)
        } else {
            tampilkanMasalahServer(activity, e, cobaLagi)
        }
    }

    fun tampilkan(activity: Activity, alasan: String, saatSimpan: () -> Unit) {
        if (activity.isFinishing) {
            return
        }

        val hostSekarang = ApiClient.ambilHost(activity)

        val etServer = EditText(activity)
        etServer.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI
        etServer.hint = "Contoh: 192.168.1.5"
        etServer.setText(hostSekarang)
        etServer.setSelection(hostSekarang.length)

        val jarak = (activity.resources.displayMetrics.density * 24).toInt()

        val wadah = FrameLayout(activity)
        wadah.setPadding(jarak, jarak / 2, jarak, 0)
        wadah.addView(etServer)

        AlertDialog.Builder(activity)
            .setTitle("Alamat server")
            .setMessage(
                "Gagal terhubung ke " + hostSekarang + ".\n\n" + alasan +
                        "\n\nPeriksa IP laptop dengan perintah ipconfig, lalu masukkan di bawah ini."
            )
            .setView(wadah)
            .setPositiveButton("Simpan") { _, _ ->
                val baru = etServer.text.toString()

                if (baru.isNotBlank()) {
                    ApiClient.simpanHost(activity, baru)
                    saatSimpan()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    /** Server terhubung, jadi yang salah bukan alamatnya. Tampilkan pesan aslinya. */
    private fun tampilkanMasalahServer(activity: Activity, e: Exception, cobaLagi: () -> Unit) {
        AlertDialog.Builder(activity)
            .setTitle("Server bermasalah")
            .setMessage(
                "Alamat server sudah benar, tetapi balasannya tidak dapat dibaca.\n\n" +
                        (e.message ?: e.toString()) +
                        "\n\nPenyebab tersering: MySQL di XAMPP belum dinyalakan."
            )
            .setPositiveButton("Coba lagi") { _, _ -> cobaLagi() }
            .setNeutralButton("Ubah alamat") { _, _ -> tampilkan(activity, e.message ?: "", cobaLagi) }
            .setNegativeButton("Tutup", null)
            .show()
    }
}
