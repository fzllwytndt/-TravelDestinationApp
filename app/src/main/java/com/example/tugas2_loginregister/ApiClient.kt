package com.example.tugas2_loginregister

import java.net.HttpURLConnection
import java.net.URL

object ApiClient {

    const val BASE_URL = "http://192.168.15.182/login_api/"

    fun post(namaFile: String, data: String): String {
        val koneksi = URL(BASE_URL + namaFile).openConnection() as HttpURLConnection
        koneksi.requestMethod = "POST"
        koneksi.doOutput = true
        koneksi.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        koneksi.connectTimeout = 5000
        koneksi.readTimeout = 5000

        koneksi.outputStream.write(data.toByteArray())

        val hasil = koneksi.inputStream.bufferedReader().readText()
        koneksi.disconnect()
        return hasil
    }
}
