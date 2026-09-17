package com.example.tugas2_loginregister.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugas2_loginregister.network.AuthRequest
import com.example.tugas2_loginregister.network.AuthResponse
import com.example.tugas2_loginregister.repository.AuthRepository
import com.example.tugas2_loginregister.utils.UiState
import kotlinx.coroutines.launch

/** Mengurus proses Login dan Register, lalu mengabarkan hasilnya ke tampilan. */
class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _kondisi = MutableLiveData<UiState<AuthResponse>?>()
    val kondisi: LiveData<UiState<AuthResponse>?> = _kondisi

    fun login(username: String, password: String) {
        kirim { repository.login(AuthRequest(username, password)) }
    }

    fun register(username: String, password: String) {
        kirim { repository.register(AuthRequest(username, password)) }
    }

    /** Hasil yang sudah ditampilkan dikosongkan supaya tidak muncul dua kali saat layar diputar. */
    fun kondisiSudahDitangani() {
        _kondisi.value = null
    }

    private fun kirim(permintaan: suspend () -> AuthResponse) {
        _kondisi.value = UiState.Loading

        viewModelScope.launch {
            try {
                _kondisi.value = UiState.Berhasil(permintaan())
            } catch (e: Exception) {
                _kondisi.value = UiState.Gagal(e.message ?: "Terjadi kesalahan", e)
            }
        }
    }
}
