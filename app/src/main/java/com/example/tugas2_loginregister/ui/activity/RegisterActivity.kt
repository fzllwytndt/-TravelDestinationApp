package com.example.tugas2_loginregister.ui.activity

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
import com.example.tugas2_loginregister.utils.UiState
import com.example.tugas2_loginregister.viewmodel.AuthViewModel
import com.example.tugas2_loginregister.viewmodel.AuthViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels { AuthViewModelFactory(this) }

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener { kirimRegister() }

        findViewById<TextView>(R.id.tvKeLogin).setOnClickListener { finish() }

        amatiHasilRegister()
    }

    private fun kirimRegister() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Helper.pesanSingkat(this, "Username dan password wajib diisi")
            return
        }

        viewModel.register(username, password)
    }

    private fun amatiHasilRegister() {
        viewModel.kondisi.observe(this) { kondisi ->
            when (kondisi) {
                is UiState.Loading -> btnRegister.isEnabled = false

                is UiState.Berhasil -> {
                    btnRegister.isEnabled = true
                    tampilkanHasil(kondisi.data)
                    viewModel.kondisiSudahDitangani()
                }

                is UiState.Gagal -> {
                    btnRegister.isEnabled = true
                    DialogServer.tangani(
                        this,
                        kondisi.penyebab ?: Exception(kondisi.pesan)
                    ) { kirimRegister() }
                    viewModel.kondisiSudahDitangani()
                }

                null -> Unit
            }
        }
    }

    private fun tampilkanHasil(balasan: AuthResponse) {
        Helper.pesanSingkat(this, balasan.message)

        if (balasan.success) {
            finish()
        }
    }
}
