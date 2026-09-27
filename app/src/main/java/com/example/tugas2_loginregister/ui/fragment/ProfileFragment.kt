package com.example.tugas2_loginregister.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.utils.SessionManager

/**
 * Halaman Profil, dipakai bersama oleh Dashboard Admin maupun Dashboard User.
 *
 * Isinya dibaca dari sesi: nama akun dan role yang sedang login. Di sini juga
 * letak tombol Logout yang menghapus sesi beserta role-nya.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val konteks = requireContext()

        view.findViewById<TextView>(R.id.tvNama).text = SessionManager.ambilUsername(konteks)

        view.findViewById<TextView>(R.id.tvRole).text =
            getString(R.string.label_role, SessionManager.ambilNamaRole(konteks))

        // Sesi dan role dihapus, lalu pengguna dikembalikan ke halaman Login.
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            SessionManager.keluar(requireActivity())
        }
    }
}
