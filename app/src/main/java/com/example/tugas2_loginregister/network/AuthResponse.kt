package com.example.tugas2_loginregister.network

import com.google.gson.annotations.SerializedName

/** Balasan `login.php` dan `register.php`. */
data class AuthResponse(

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("username")
    val username: String = "",

    /**
     * Role akun: "admin" atau "user".
     * Dikirim Backend setelah login berhasil, lalu disimpan ke sesi oleh aplikasi
     * untuk menentukan dashboard mana yang dibuka.
     */
    @SerializedName("role")
    val role: String = "user"
)
