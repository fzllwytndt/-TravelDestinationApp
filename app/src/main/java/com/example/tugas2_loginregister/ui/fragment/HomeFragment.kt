package com.example.tugas2_loginregister.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.model.Wisata
import com.example.tugas2_loginregister.ui.activity.DetailWisataActivity
import com.example.tugas2_loginregister.ui.adapter.WisataAdapter
import com.example.tugas2_loginregister.utils.DialogServer
import com.example.tugas2_loginregister.utils.Helper
import com.example.tugas2_loginregister.utils.SessionManager
import com.example.tugas2_loginregister.utils.UiState
import com.example.tugas2_loginregister.viewmodel.WisataViewModel

class HomeFragment : Fragment() {

    private val viewModel: WisataViewModel by viewModels()

    private lateinit var svCari: SearchView
    private lateinit var rvWisata: RecyclerView
    private lateinit var pbAwal: ProgressBar
    private lateinit var wadahMuatLagi: View
    private lateinit var tvPesan: TextView
    private lateinit var tvInfoBawah: TextView

    private lateinit var adapter: WisataAdapter

    private val handlerCari = Handler(Looper.getMainLooper())
    private var tundaCari: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        siapkanHeader(view)
        hubungkanView(view)
        siapkanDaftar()
        siapkanPencarian()
        pasangScrollListener()
        amatiData()
    }

    private fun siapkanHeader(view: View) {
        val username = SessionManager.ambilUsername(requireContext())
        view.findViewById<TextView>(R.id.tvWelcome).text = "Selamat Datang, $username"
    }

    private fun hubungkanView(view: View) {
        svCari = view.findViewById(R.id.svCari)
        rvWisata = view.findViewById(R.id.rvWisata)
        pbAwal = view.findViewById(R.id.pbAwal)
        wadahMuatLagi = view.findViewById(R.id.wadahMuatLagi)
        tvPesan = view.findViewById(R.id.tvPesan)
        tvInfoBawah = view.findViewById(R.id.tvInfoBawah)
    }

    private fun siapkanDaftar() {
        adapter = WisataAdapter { wisata -> bukaDetail(wisata) }

        rvWisata.layoutManager = LinearLayoutManager(requireContext())
        rvWisata.adapter = adapter

        tvPesan.setOnClickListener { viewModel.muatUlang() }
    }

    private fun bukaDetail(wisata: Wisata) {
        val intent = Intent(requireContext(), DetailWisataActivity::class.java)
        intent.putExtra(DetailWisataActivity.KUNCI_ID, wisata.id)
        startActivity(intent)
    }

    private fun siapkanPencarian() {
        svCari.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                batalkanTundaCari()
                svCari.clearFocus()
                viewModel.cari(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                jadwalkanPencarian(newText.orEmpty())
                return true
            }
        })
    }

    private fun jadwalkanPencarian(kunci: String) {
        batalkanTundaCari()

        val tugas = Runnable { viewModel.cari(kunci) }

        tundaCari = tugas
        handlerCari.postDelayed(tugas, JEDA_KETIK)
    }

    private fun batalkanTundaCari() {
        tundaCari?.let { handlerCari.removeCallbacks(it) }
        tundaCari = null
    }

    private fun pasangScrollListener() {
        rvWisata.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                perbaruiInfoBawah()

                if (dy > 0 && sudahDekatBawah()) {
                    viewModel.muatBerikutnya()
                }
            }
        })
    }

    private fun sudahDekatBawah(): Boolean {
        val layoutManager = rvWisata.layoutManager as LinearLayoutManager
        val posisiTerakhir = layoutManager.findLastVisibleItemPosition()

        return posisiTerakhir >= adapter.itemCount - 3
    }

    private fun amatiData() {
        viewModel.kondisi.observe(viewLifecycleOwner) { kondisi ->
            when (kondisi) {
                is UiState.Loading -> tampilkanLoading()
                is UiState.Berhasil -> tampilkanData(kondisi.data)
                is UiState.Gagal -> tampilkanGagal(kondisi)
            }
        }

        viewModel.semuaSudahDimuat.observe(viewLifecycleOwner) { selesai ->
            tvInfoBawah.text = "Semua data sudah ditampilkan"
            Helper.tampilJika(tvInfoBawah, selesai && adapter.itemCount > 0)
            rvWisata.post { perbaruiInfoBawah() }
        }

        viewModel.muatPertamaKali()
    }

    private fun tampilkanLoading() {
        if (viewModel.jumlahData == 0) {
            adapter.submitList(emptyList())

            Helper.sembunyi(tvPesan, wadahMuatLagi, tvInfoBawah)
            Helper.tampil(pbAwal)
        } else {
            Helper.tampil(wadahMuatLagi)
        }
    }

    private fun tampilkanData(daftar: List<Wisata>) {
        Helper.sembunyi(pbAwal, wadahMuatLagi)

        adapter.submitList(daftar)

        if (daftar.isEmpty()) {
            tvPesan.text =
                if (viewModel.kataKunci.isBlank()) "Belum ada data wisata"
                else "Wisata \"${viewModel.kataKunci}\" tidak ditemukan"

            Helper.tampil(tvPesan)
            return
        }

        Helper.sembunyi(tvPesan)
        rvWisata.post { perbaruiInfoBawah() }
    }

    private fun tampilkanGagal(gagal: UiState.Gagal) {
        Helper.sembunyi(pbAwal, wadahMuatLagi)

        if (viewModel.jumlahData > 0) {
            Helper.pesanSingkat(requireContext(), "Gagal memuat data berikutnya")
            return
        }

        tvPesan.text = "Gagal memuat data.\nPeriksa koneksi, lalu ketuk di sini untuk mencoba lagi."
        Helper.tampil(tvPesan)

        if (viewModel.kataKunci.isBlank() && gagal.penyebab != null) {
            DialogServer.tangani(requireActivity(), gagal.penyebab) { viewModel.muatUlang() }
        }
    }

    /** Keterangan di bawah daftar hanya muncul ketika data terakhir benar-benar terlihat. */
    private fun perbaruiInfoBawah() {
        if (viewModel.semuaSudahDimuat.value != true || adapter.itemCount == 0) {
            Helper.sembunyi(tvInfoBawah)
            return
        }

        val layoutManager = rvWisata.layoutManager as LinearLayoutManager
        val posisiTerakhir = layoutManager.findLastVisibleItemPosition()

        Helper.tampilJika(tvInfoBawah, posisiTerakhir >= adapter.itemCount - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        batalkanTundaCari()
    }

    companion object {
        private const val JEDA_KETIK = 400L
    }
}
