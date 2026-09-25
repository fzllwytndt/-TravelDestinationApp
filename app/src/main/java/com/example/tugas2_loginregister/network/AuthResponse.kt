package com.example.tugas2_loginregister.network

import com.google.gson.annotations.SerializedName

/** Balasan `login.php` dan `register.php`. */
data class AuthResponse(

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("username")
    val username: String = ""
)
