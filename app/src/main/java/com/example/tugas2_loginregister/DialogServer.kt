package com.example.tugas2_loginregister

import android.app.Activity
import android.text.InputType
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog

object DialogServer {

    fun tampilkan(activity: Activity, saatSimpan: () -> Unit) {
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
                "Gagal terhubung ke " + hostSekarang +
                        ".\n\nPeriksa IP laptop dengan perintah ipconfig, lalu masukkan di bawah ini."
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
}
