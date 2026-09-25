package com.example.tugas2_loginregister.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.data.local.room.FavoriteWisata
import com.example.tugas2_loginregister.ui.activity.DetailWisataActivity
import com.example.tugas2_loginregister.ui.adapter.FavoriteWisataAdapter
import com.example.tugas2_loginregister.utils.Helper
import com.example.tugas2_loginregister.viewmodel.FavoriteViewModel

/** Menampilkan seluruh wisata yang tersimpan di Room Database. */
class FavoriteFragment : Fragment() {

    private val viewModel: FavoriteViewModel by viewModels()

    private lateinit var rvFavorit: RecyclerView
    private lateinit var tvKosong: View

    private lateinit var adapter: FavoriteWisataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavorit = view.findViewById(R.id.rvFavorit)
        tvKosong = view.findViewById(R.id.tvKosong)

        siapkanDaftar()
        amatiDaftarFavorit()
    }

    private fun siapkanDaftar() {
        adapter = FavoriteWisataAdapter { favorit -> bukaDetail(favorit) }

        rvFavorit.layoutManager = LinearLayoutManager(requireContext())
        rvFavorit.adapter = adapter
    }

    private fun bukaDetail(favorit: FavoriteWisata) {
        val intent = Intent(requireContext(), DetailWisataActivity::class.java)
        intent.putExtra(DetailWisataActivity.KUNCI_ID, favorit.id)
        startActivity(intent)
    }

    /**
     * Daftar diambil dalam bentuk LiveData, jadi ketika ada wisata yang di-Unlike
     * datanya langsung hilang dari halaman ini tanpa perlu dimuat ulang.
     */
    private fun amatiDaftarFavorit() {
        viewModel.daftarFavorit.observe(viewLifecycleOwner) { daftar ->
            adapter.submitList(daftar)

            Helper.tampilJika(tvKosong, daftar.isEmpty())
        }
    }
}
