package com.example.tugas2_loginregister.repository

import android.content.Context
import com.example.tugas2_loginregister.network.ApiClient
import com.example.tugas2_loginregister.network.AuthRequest
import com.example.tugas2_loginregister.network.AuthResponse

/** Penghubung antara ViewModel dengan API Login dan Register. */
class AuthRepository(private val context: Context) {

    suspend fun login(permintaan: AuthRequest): AuthResponse {
        return ApiClient.panggil {
            ApiClient.layanan(context).login(permintaan.username, permintaan.password)
        }
    }

    suspend fun register(permintaan: AuthRequest): AuthResponse {
        return ApiClient.panggil {
            ApiClient.layanan(context).register(permintaan.username, permintaan.password)
        }
    }
}
