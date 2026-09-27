package com.example.tugas2_loginregister.network

/**
 * Data yang dikirim ke server saat pengguna melakukan Login atau Register.
 *
 * [role] hanya dipakai saat Register, diisi dari pilihan "Daftar sebagai"
 * pada halaman Register. Saat Login isinya diabaikan karena role yang berlaku
 * adalah yang tersimpan di database.
 */
data class AuthRequest(
    val username: String,
    val password: String,
    val role: String = "user"
)
