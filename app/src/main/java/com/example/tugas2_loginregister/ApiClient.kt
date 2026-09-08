package com.example.tugas2_loginregister

import android.content.Context
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object ApiClient {

    const val HOST_DEFAULT = "192.168.15.181"

    private const val NAMA_PREF = "pengaturan_server"
    private const val KUNCI_HOST = "alamat_server"
    private const val WAKTU_TUNGGU = 5000

    fun baseUrl(context: Context): String {
        return "http://" + ambilHost(context) + "/login_api/"
    }

    fun ambilHost(context: Context): String {
        val tersimpan = pref(context).getString(KUNCI_HOST, "") ?: ""

        return if (tersimpan.isBlank()) HOST_DEFAULT else tersimpan
    }

    fun simpanHost(context: Context, host: String) {
        pref(context).edit()
            .putString(KUNCI_HOST, rapikanHost(host))
            .apply()
    }

    fun post(context: Context, namaFile: String, data: String): String {
        val koneksi = bukaKoneksi(context, namaFile, "POST")
        koneksi.doOutput = true
        koneksi.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        koneksi.outputStream.write(data.toByteArray())

        return bacaHasil(koneksi)
    }

    fun get(context: Context, namaFile: String): String {
        return bacaHasil(bukaKoneksi(context, namaFile, "GET"))
    }

    fun dataForm(username: String, password: String): String {
        return "username=" + URLEncoder.encode(username, "UTF-8") +
                "&password=" + URLEncoder.encode(password, "UTF-8")
    }

    private fun bukaKoneksi(context: Context, namaFile: String, metode: String): HttpURLConnection {
        val koneksi = URL(baseUrl(context) + namaFile).openConnection() as HttpURLConnection
        koneksi.requestMethod = metode
        koneksi.connectTimeout = WAKTU_TUNGGU
        koneksi.readTimeout = WAKTU_TUNGGU

        return koneksi
    }

    private fun bacaHasil(koneksi: HttpURLConnection): String {
        val hasil = koneksi.inputStream.bufferedReader().readText()
        koneksi.disconnect()

        return hasil
    }

    private fun pref(context: Context) =
        context.applicationContext.getSharedPreferences(NAMA_PREF, Context.MODE_PRIVATE)

    private fun rapikanHost(host: String): String {
        return host.trim()
            .removePrefix("http://")
            .removePrefix("https://")
            .trim('/')
    }
}
