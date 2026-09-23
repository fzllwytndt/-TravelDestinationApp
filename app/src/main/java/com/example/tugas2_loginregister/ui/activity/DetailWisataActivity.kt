package com.example.tugas2_loginregister.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.model.Wisata
import com.example.tugas2_loginregister.utils.Helper
import com.example.tugas2_loginregister.utils.UiState
import com.example.tugas2_loginregister.viewmodel.DetailWisataViewModel
import com.google.android.material.button.MaterialButton
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
    private lateinit var barisAksi: View
    private lateinit var btnEdit: MaterialButton
    private lateinit var btnHapus: MaterialButton

    private var idWisata = 0
    private var wisataSekarang: Wisata? = null
    private var sudahFavorit = false
    private var dataBerubah = false

    private val launcherEdit = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { hasil ->
        if (hasil.resultCode == RESULT_OK) {
            dataBerubah = true
            setResult(RESULT_OK)
            viewModel.muatDetail(idWisata)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_wisata)

        hubungkanView()
        siapkanTombolKembali()

        idWisata = intent.getIntExtra(KUNCI_ID, 0)

        tvPesan.setOnClickListener { viewModel.muatDetail(idWisata) }
        fabFavorit.setOnClickListener { ubahFavorit() }
        btnEdit.setOnClickListener { bukaHalamanEdit() }
        btnHapus.setOnClickListener { konfirmasiHapus() }

        amatiDetail()
        amatiStatusFavorit()
        amatiStatusHapus()

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
        barisAksi = findViewById(R.id.barisAksi)
        btnEdit = findViewById(R.id.btnEdit)
        btnHapus = findViewById(R.id.btnHapus)
    }

    private fun siapkanTombolKembali() {
        val barisAtas = findViewById<View>(R.id.barisAtas)

        ViewCompat.setOnApplyWindowInsetsListener(barisAtas) { view, jarakSistem ->
            val atas = jarakSistem.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, atas, 0, 0)
            jarakSistem
        }

        findViewById<ImageButton>(R.id.btnKembali).setOnClickListener {
            if (dataBerubah) setResult(RESULT_OK)
            finish()
        }
    }

    private fun amatiDetail() {
        viewModel.kondisi.observe(this) { kondisi ->
            when (kondisi) {
                is UiState.Loading -> {
                    Helper.sembunyi(isiDetail, tvPesan, fabFavorit, barisAksi)
                    Helper.tampil(pbLoading)
                }

                is UiState.Berhasil -> tampilkanDetail(kondisi.data)

                is UiState.Gagal -> tampilkanPesan(kondisi.pesan)
            }
        }
    }

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

    private fun amatiStatusHapus() {
        viewModel.kondisiHapus.observe(this) { kondisi ->
            when (kondisi) {
                is UiState.Loading -> {
                    Helper.tampil(pbLoading)
                    btnEdit.isEnabled = false
                    btnHapus.isEnabled = false
                }

                is UiState.Berhasil -> {
                    Helper.sembunyi(pbLoading)
                    Toast.makeText(this, kondisi.data, Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }

                is UiState.Gagal -> {
                    Helper.sembunyi(pbLoading)
                    btnEdit.isEnabled = true
                    btnHapus.isEnabled = true
                    Toast.makeText(this, kondisi.pesan, Toast.LENGTH_LONG).show()
                    viewModel.resetKondisiHapus()
                }

                null -> {
                    Helper.sembunyi(pbLoading)
                    btnEdit.isEnabled = true
                    btnHapus.isEnabled = true
                }
            }
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

    private fun bukaHalamanEdit() {
        val wisata = wisataSekarang ?: return
        val intent = Intent(this, EditWisataActivity::class.java)
        intent.putExtra(EditWisataActivity.KUNCI_WISATA, wisata)
        intent.putExtra(EditWisataActivity.KUNCI_ID, idWisata)
        launcherEdit.launch(intent)
    }

    private fun konfirmasiHapus() {
        AlertDialog.Builder(this)
            .setTitle("Hapus Wisata")
            .setMessage(getString(R.string.konfirmasi_hapus))
            .setPositiveButton(getString(R.string.ya_hapus)) { _, _ ->
                viewModel.hapusWisata(idWisata)
            }
            .setNegativeButton(getString(R.string.batal), null)
            .show()
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

        Helper.tampil(isiDetail, fabFavorit, barisAksi)
    }

    private fun tampilkanPesan(teks: String) {
        Helper.sembunyi(pbLoading, isiDetail, fabFavorit, barisAksi)

        tvPesan.text = teks
        Helper.tampil(tvPesan)
    }

    companion object {
        const val KUNCI_ID = "id"
    }
}
