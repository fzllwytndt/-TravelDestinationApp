package com.example.tugas2_loginregister.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.utils.SessionManager

/**
 * Halaman pertama yang muncul saat aplikasi dibuka.
 *
 * Tugasnya memeriksa sesi:
 * - Sesi tidak tersedia  -> halaman Login.
 * - Sesi tersedia        -> dashboard sesuai role yang tersimpan.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            bukaHalamanBerikutnya()
        }, LAMA_TAMPIL)
    }

    private fun bukaHalamanBerikutnya() {
        if (SessionManager.sudahLogin(this)) {
            // Role dibaca dari sesi, jadi pengguna tidak perlu login ulang.
            SessionManager.bukaDashboard(this)
            return
        }

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    companion object {
        private const val LAMA_TAMPIL = 2000L
    }
}
