package com.example.tugas2_loginregister

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(username, password)
        }

        findViewById<TextView>(R.id.tvKeRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login(username: String, password: String) {
        Thread {
            try {
                val hasil = ApiClient.post(this, "login.php", ApiClient.dataForm(username, password))
                val json = JSONObject(hasil)

                runOnUiThread {
                    tampilkanHasil(json)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    DialogServer.tampilkan(this) {
                        login(username, password)
                    }
                }
            }
        }.start()
    }

    private fun tampilkanHasil(json: JSONObject) {
        Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show()

        if (json.getBoolean("success")) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", json.getString("username"))
            startActivity(intent)
            finish()
        }
    }
}
