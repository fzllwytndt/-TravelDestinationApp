package com.example.tugas2_loginregister.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.model.Wisata
import com.example.tugas2_loginregister.utils.Helper
import com.example.tugas2_loginregister.utils.UiState
import com.example.tugas2_loginregister.viewmodel.DetailWisataViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailWisataActivity : AppCompatActivity() {

    private val viewModel: DetailWisataViewModel by viewModels()

    private lateinit var isiDetail: View
    private lateinit var ivFoto: ImageView
    private lateinit var tvKategori: TextView
    private lateinit var tvNama: TextView
    private lateinit var tvLokasi: TextView
    private lateinit var tvHarga: TextView
    private lateinit var tvDeskripsi: TextView
    private lateinit var pbLoading: ProgressBar
    private lateinit var tvPesan: TextView
    private lateinit var fabFavorit: FloatingActionButton

    private var idWisata = 0

    /** Data wisata yang sedang ditampilkan, dipakai saat tombol Like ditekan. */
    private var wisataSekarang: Wisata? = null

    private var sudahFavorit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_wisata)

        hubungkanView()
        siapkanTombolKembali()

        idWisata = intent.getIntExtra(KUNCI_ID, 0)

        tvPesan.setOnClickListener { viewModel.muatDetail(idWisata) }
        fabFavorit.setOnClickListener { ubahFavorit() }

        amatiDetail()
        amatiStatusFavorit()

        if (savedInstanceState == null) {
            viewModel.muatDetail(idWisata)
        }
    }

    private fun hubungkanView() {
        isiDetail = findViewById(R.id.isiDetail)
        ivFoto = findViewById(R.id.ivFoto)
        tvKategori = findViewById(R.id.tvKategori)
        tvNama = findViewById(R.id.tvNama)
        tvLokasi = findViewById(R.id.tvLokasi)
        tvHarga = findViewById(R.id.tvHarga)
        tvDeskripsi = findViewById(R.id.tvDeskripsi)
        pbLoading = findViewById(R.id.pbLoading)
        tvPesan = findViewById(R.id.tvPesan)
        fabFavorit = findViewById(R.id.fabFavorit)
    }

    /** Tombol kembali diletakkan tepat di bawah status bar. */
    private fun siapkanTombolKembali() {
        val barisAtas = findViewById<View>(R.id.barisAtas)

        ViewCompat.setOnApplyWindowInsetsListener(barisAtas) { view, jarakSistem ->
            val atas = jarakSistem.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, atas, 0, 0)
            jarakSistem
        }

        findViewById<ImageButton>(R.id.btnKembali).setOnClickListener { finish() }
    }

    private fun amatiDetail() {
        viewModel.kondisi.observe(this) { kondisi ->
            when (kondisi) {
                is UiState.Loading -> {
                    Helper.sembunyi(isiDetail, tvPesan, fabFavorit)
                    Helper.tampil(pbLoading)
                }

                is UiState.Berhasil -> tampilkanDetail(kondisi.data)

                is UiState.Gagal -> tampilkanPesan(kondisi.pesan)
            }
        }
    }

    /** Ikon love mengikuti isi Room Database, jadi warnanya selalu sesuai keadaan terakhir. */
    private fun amatiStatusFavorit() {
        viewModel.statusFavorit(idWisata).observe(this) { favorit ->
            sudahFavorit = favorit

            fabFavorit.setImageResource(
                if (favorit) R.drawable.ic_favorite_red else R.drawable.ic_favorite_black
            )

            fabFavorit.contentDescription =
                if (favorit) getString(R.string.hapus_favorit) else getString(R.string.tambah_favorit)
        }
    }

    private fun ubahFavorit() {
        val wisata = wisataSekarang ?: return

        viewModel.ubahFavorit(wisata, sudahFavorit)

        Helper.pesanSingkat(
            this,
            if (sudahFavorit) "Dihapus dari favorit" else "Ditambahkan ke favorit"
        )
    }

    private fun tampilkanDetail(wisata: Wisata) {
        wisataSekarang = wisata

        Helper.sembunyi(pbLoading, tvPesan)

        Helper.muatGambar(ivFoto, wisata.fotoUrl)

        tvKategori.text = wisata.kategori
        tvNama.text = wisata.namaWisata
        tvLokasi.text = wisata.lokasi
        tvHarga.text = Helper.rupiah(wisata.hargaTiket)
        tvDeskripsi.text = wisata.deskripsi

        Helper.tampil(isiDetail, fabFavorit)
    }

    private fun tampilkanPesan(teks: String) {
        Helper.sembunyi(pbLoading, isiDetail, fabFavorit)

        tvPesan.text = teks
        Helper.tampil(tvPesan)
    }

    companion object {
        const val KUNCI_ID = "id"
    }
}
