package com.example.tugas2_loginregister

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URLEncoder

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvKeRegister = findViewById<TextView>(R.id.tvKeRegister)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(username, password)
        }

        tvKeRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login(username: String, password: String) {
        Thread {
            try {
                val data = "username=" + URLEncoder.encode(username, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8")

                val hasil = ApiClient.post("login.php", data)
                val json = JSONObject(hasil)

                runOnUiThread {
                    Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show()

                    if (json.getBoolean("success")) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("username", json.getString("username"))
                        startActivity(intent)
                        finish()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
