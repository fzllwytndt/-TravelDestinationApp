package com.example.tugas2_loginregister.network

/** Data yang dikirim ke server saat pengguna melakukan Login atau Register. */
data class AuthRequest(
    val username: String,
    val password: String
)
