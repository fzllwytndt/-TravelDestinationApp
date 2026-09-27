package com.example.tugas2_loginregister.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.tugas2_loginregister.ui.activity.AdminWisataActivity
import com.example.tugas2_loginregister.ui.activity.LoginActivity
import com.example.tugas2_loginregister.ui.activity.MainActivity

/**
 * Penyimpan status login supaya pengguna tidak perlu login ulang setiap membuka aplikasi.
 *
 * Sejak Tugas 9 sesi juga menyimpan role akun yang sedang login. Role itulah yang
 * dipakai aplikasi untuk memilih dashboard: Admin atau User.
 */
object SessionManager {

    const val ROLE_ADMIN = "admin"
    const val ROLE_USER = "user"

    private const val NAMA_PREF = "sesi_login"
    private const val KUNCI_LOGIN = "sudah_login"
    private const val KUNCI_USERNAME = "username"
    private const val KUNCI_ROLE = "role"

    /** Dipanggil sekali saja, yaitu ketika Login berhasil. */
    fun simpan(context: Context, username: String, role: String) {
        pref(context).edit()
            .putBoolean(KUNCI_LOGIN, true)
            .putString(KUNCI_USERNAME, username)
            .putString(KUNCI_ROLE, rapikanRole(role))
            .apply()
    }

    fun sudahLogin(context: Context): Boolean {
        return pref(context).getBoolean(KUNCI_LOGIN, false)
    }

    fun ambilUsername(context: Context): String {
        return pref(context).getString(KUNCI_USERNAME, "") ?: ""
    }

    fun ambilRole(context: Context): String {
        return pref(context).getString(KUNCI_ROLE, ROLE_USER) ?: ROLE_USER
    }

    fun adalahAdmin(context: Context): Boolean {
        return ambilRole(context) == ROLE_ADMIN
    }

    /** Dipakai untuk ditampilkan pada halaman Profil. */
    fun ambilNamaRole(context: Context): String {
        return if (adalahAdmin(context)) "Admin" else "User"
    }

    /** Dashboard yang sesuai dengan role akun yang sedang login. */
    fun halamanDashboard(context: Context): Class<*> {
        return if (adalahAdmin(context)) AdminWisataActivity::class.java
        else MainActivity::class.java
    }

    /** Membuka dashboard sesuai role, dipakai Splash dan Login. */
    fun bukaDashboard(activity: Activity) {
        activity.startActivity(Intent(activity, halamanDashboard(activity)))
        activity.finish()
    }

    /** Seluruh isi sesi dihapus, termasuk status login dan role. */
    fun hapus(context: Context) {
        pref(context).edit().clear().apply()
    }

    /**
     * Logout: sesi dihapus lalu pengguna dikembalikan ke halaman Login.
     * Riwayat halaman ikut dibersihkan supaya tombol Back tidak bisa
     * membawa pengguna masuk kembali ke dashboard.
     */
    fun keluar(activity: Activity) {
        hapus(activity)

        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        activity.startActivity(intent)
        activity.finish()
    }

    /** Role dari server dibuat huruf kecil, dan nilai tak dikenal dianggap user biasa. */
    private fun rapikanRole(role: String): String {
        return if (role.trim().lowercase() == ROLE_ADMIN) ROLE_ADMIN else ROLE_USER
    }

    private fun pref(context: Context) =
        context.applicationContext.getSharedPreferences(NAMA_PREF, Context.MODE_PRIVATE)
}
