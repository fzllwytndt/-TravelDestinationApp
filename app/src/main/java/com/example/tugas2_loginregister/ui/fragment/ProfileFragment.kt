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

        view.findViewById<TextView>(R.id.tvNama).text = SessionManager.ambilUsername(requireContext())

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            SessionManager.keluar(requireActivity())
        }
    }
}
