package com.example.tugas2_loginregister.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.ui.fragment.FavoriteFragment
import com.example.tugas2_loginregister.ui.fragment.HomeFragment
import com.example.tugas2_loginregister.ui.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        beriRuangStatusBar(findViewById(R.id.main))

        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.menuHome
            bukaHalaman(HomeFragment())
        }

        bottomNav.setOnItemSelectedListener { menu ->
            bukaHalaman(
                when (menu.itemId) {
                    R.id.menuFavorit -> FavoriteFragment()
                    R.id.menuProfil -> ProfileFragment()
                    else -> HomeFragment()
                }
            )
            true
        }
    }

    /** Menambah jarak di atas layar supaya konten tidak tertutup status bar. */
    private fun beriRuangStatusBar(wadah: View) {
        ViewCompat.setOnApplyWindowInsetsListener(wadah) { view, jarakSistem ->
            val atas = jarakSistem.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, atas, 0, 0)
            jarakSistem
        }
    }

    private fun bukaHalaman(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.wadahFragment, fragment)
            .commit()
    }
}
