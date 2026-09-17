package com.example.tugas2_loginregister.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.network.AuthResponse
import com.example.tugas2_loginregister.utils.DialogServer
import com.example.tugas2_loginregister.utils.Helper
import com.example.tugas2_loginregister.utils.SessionManager
import com.example.tugas2_loginregister.utils.UiState
import com.example.tugas2_loginregister.viewmodel.AuthViewModel
import com.example.tugas2_loginregister.viewmodel.AuthViewModelFactory

class LoginActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels { AuthViewModelFactory(this) }

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener { kirimLogin() }

        findViewById<TextView>(R.id.tvKeRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        amatiHasilLogin()
    }

    private fun kirimLogin() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Helper.pesanSingkat(this, "Username dan password wajib diisi")
            return
        }

        viewModel.login(username, password)
    }

    private fun amatiHasilLogin() {
        viewModel.kondisi.observe(this) { kondisi ->
            when (kondisi) {
                is UiState.Loading -> btnLogin.isEnabled = false

                is UiState.Berhasil -> {
                    btnLogin.isEnabled = true
                    tampilkanHasil(kondisi.data)
                    viewModel.kondisiSudahDitangani()
                }

                is UiState.Gagal -> {
                    btnLogin.isEnabled = true
                    tampilkanGagal(kondisi)
                    viewModel.kondisiSudahDitangani()
                }

                null -> Unit
            }
        }
    }

    private fun tampilkanHasil(balasan: AuthResponse) {
        Helper.pesanSingkat(this, balasan.message)

        if (balasan.success) {
            SessionManager.simpan(this, balasan.username)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun tampilkanGagal(gagal: UiState.Gagal) {
        val penyebab = gagal.penyebab ?: Exception(gagal.pesan)

        DialogServer.tangani(this, penyebab) { kirimLogin() }
    }
}
