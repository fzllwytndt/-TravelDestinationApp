package com.example.tugas2_loginregister.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Data wisata yang dikirim server.
 * Nama kolom pada JSON memakai garis bawah, jadi dipasangkan memakai [SerializedName].
 */
data class Wisata(

    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("nama_wisata")
    val namaWisata: String = "",

    @SerializedName("kategori")
    val kategori: String = "",

    @SerializedName("lokasi")
    val lokasi: String = "",

    @SerializedName("harga_tiket")
    val hargaTiket: Int = 0,

    @SerializedName("deskripsi")
    val deskripsi: String = "",

    @SerializedName("foto")
    val foto: String = "",

    @SerializedName("foto_url")
    val fotoUrl: String = ""
) : Serializable
