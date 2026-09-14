package com.example.tugas2_loginregister

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            bukaHalamanBerikutnya()
        }, LAMA_TAMPIL)
    }

    private fun bukaHalamanBerikutnya() {
        val tujuan =
            if (Sesi.sudahLogin(this)) MainActivity::class.java
            else LoginActivity::class.java

        startActivity(Intent(this, tujuan))
        finish()
    }

    companion object {
        private const val LAMA_TAMPIL = 2000L
    }
}
