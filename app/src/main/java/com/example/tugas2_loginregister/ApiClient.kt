package com.example.tugas2_loginregister

import java.net.HttpURLConnection
import java.net.URL

object ApiClient {

    // Ganti IP di bawah ini dengan IP laptop yang menjalankan XAMPP.
    // Cek lewat perintah ipconfig, bagian IPv4 Address.
    const val BASE_URL = "http://192.168.18.154/login_api/"

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

    // Dipakai untuk mengambil data wisata per halaman.
    // Contoh pemanggilan: get("wisata.php?page=1")
    fun get(namaFile: String): String {
        val koneksi = URL(BASE_URL + namaFile).openConnection() as HttpURLConnection
        koneksi.requestMethod = "GET"
        koneksi.connectTimeout = 5000
        koneksi.readTimeout = 5000

        val hasil = koneksi.inputStream.bufferedReader().readText()
        koneksi.disconnect()
        return hasil
    }
}
