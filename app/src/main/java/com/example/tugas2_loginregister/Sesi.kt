package com.example.tugas2_loginregister

import android.app.Activity
import android.content.Context
import android.content.Intent

object Sesi {

    private const val NAMA_PREF = "sesi_login"
    private const val KUNCI_LOGIN = "sudah_login"
    private const val KUNCI_USERNAME = "username"

    fun simpan(context: Context, username: String) {
        pref(context).edit()
            .putBoolean(KUNCI_LOGIN, true)
            .putString(KUNCI_USERNAME, username)
            .apply()
    }

    fun sudahLogin(context: Context): Boolean {
        return pref(context).getBoolean(KUNCI_LOGIN, false)
    }

    fun ambilUsername(context: Context): String {
        return pref(context).getString(KUNCI_USERNAME, "") ?: ""
    }

    fun hapus(context: Context) {
        pref(context).edit().clear().apply()
    }

    fun keluar(activity: Activity) {
        hapus(activity)

        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        activity.startActivity(intent)
        activity.finish()
    }

    private fun pref(context: Context) =
        context.applicationContext.getSharedPreferences(NAMA_PREF, Context.MODE_PRIVATE)
}
