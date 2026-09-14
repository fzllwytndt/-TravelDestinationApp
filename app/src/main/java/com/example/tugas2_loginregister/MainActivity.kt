package com.example.tugas2_loginregister

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

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

        if (savedInstanceState == null) {
            bukaHalaman(HomeFragment())
            bottomNav.selectedItemId = R.id.menuHome
        }
    }

    private fun bukaHalaman(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.wadahFragment, fragment)
            .commit()
    }
}
