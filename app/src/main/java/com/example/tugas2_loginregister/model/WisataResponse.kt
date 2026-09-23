package com.example.tugas2_loginregister.model

import com.google.gson.annotations.SerializedName

/** Balasan `wisata.php`, yaitu daftar wisata beserta keterangan halamannya. */
data class WisataResponse(

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: List<Wisata> = emptyList(),

    @SerializedName("meta")
    val meta: MetaWisata = MetaWisata()
)

/** Keterangan halaman yang dipakai fitur pagination. */
data class MetaWisata(

    @SerializedName("total_page")
    val totalPage: Int = 1,

    @SerializedName("current_page")
    val currentPage: Int = 1
)

/** Balasan `wisata_detail.php`, yaitu satu data wisata. */
data class WisataDetailResponse(

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: Wisata? = null
)

/** Balasan aksi CRUD (Tambah, Edit, Hapus data). */
data class WisataActionResponse(

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: Wisata? = null
)
