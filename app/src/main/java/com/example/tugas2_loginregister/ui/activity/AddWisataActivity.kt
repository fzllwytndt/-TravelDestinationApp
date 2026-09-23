package com.example.tugas2_loginregister.ui.activity

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.utils.Helper
import com.example.tugas2_loginregister.utils.UiState
import com.example.tugas2_loginregister.viewmodel.AddWisataViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class AddWisataActivity : AppCompatActivity() {

    private val viewModel: AddWisataViewModel by viewModels()

    private lateinit var btnKembali: ImageButton
    private lateinit var cardFoto: MaterialCardView
    private lateinit var ivPreviewFoto: ImageView
    private lateinit var tvLabelFoto: TextView
    private lateinit var etNamaWisata: EditText
    private lateinit var etKategori: EditText
    private lateinit var etLokasi: EditText
    private lateinit var etHargaTiket: EditText
    private lateinit var etFoto: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var btnSimpan: MaterialButton
    private lateinit var pbLoading: ProgressBar

    /** Foto dari galeri yang siap diunggah. Null berarti pengguna belum memilih foto. */
    private var fotoTerpilih: Uri? = null

    private val pemilihFoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            fotoTerpilih = uri
            tampilkanPratinjau(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wisata)

        hubungkanView()
        siapkanTombolKembali()
        pulihkanFotoTerpilih(savedInstanceState)

        cardFoto.setOnClickListener { bukaGaleri() }
        btnSimpan.setOnClickListener { simpanData() }

        amatiViewModel()
    }

    private fun hubungkanView() {
        btnKembali = findViewById(R.id.btnKembali)
        cardFoto = findViewById(R.id.cardFoto)
        ivPreviewFoto = findViewById(R.id.ivPreviewFoto)
        tvLabelFoto = findViewById(R.id.tvLabelFoto)
        etNamaWisata = findViewById(R.id.etNamaWisata)
        etKategori = findViewById(R.id.etKategori)
        etLokasi = findViewById(R.id.etLokasi)
        etHargaTiket = findViewById(R.id.etHargaTiket)
        etFoto = findViewById(R.id.etFoto)
        etDeskripsi = findViewById(R.id.etDeskripsi)
        btnSimpan = findViewById(R.id.btnSimpan)
        pbLoading = findViewById(R.id.pbLoading)
    }

    private fun siapkanTombolKembali() {
        val headerAdd = findViewById<View>(R.id.headerAdd)
        ViewCompat.setOnApplyWindowInsetsListener(headerAdd) { view, jarakSistem ->
            val atas = jarakSistem.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, atas, 0, 0)
            jarakSistem
        }

        btnKembali.setOnClickListener { finish() }
    }

    /** Foto pilihan disimpan sendiri supaya tidak hilang saat layar diputar. */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KUNCI_FOTO_TERPILIH, fotoTerpilih?.toString())
    }

    private fun pulihkanFotoTerpilih(savedInstanceState: Bundle?) {
        val tersimpan = savedInstanceState?.getString(KUNCI_FOTO_TERPILIH) ?: return

        fotoTerpilih = Uri.parse(tersimpan)
        tampilkanPratinjau(Uri.parse(tersimpan))
    }

    private fun bukaGaleri() {
        pemilihFoto.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private fun tampilkanPratinjau(uri: Uri) {
        Helper.muatGambar(ivPreviewFoto, uri)
        Helper.tampil(ivPreviewFoto)
        tvLabelFoto.setText(R.string.ganti_foto_wisata)
    }

    private fun simpanData() {
        val nama = etNamaWisata.text.toString()
        val kategori = etKategori.text.toString()
        val lokasi = etLokasi.text.toString()
        val harga = etHargaTiket.text.toString()
        val foto = etFoto.text.toString()
        val deskripsi = etDeskripsi.text.toString()

        if (nama.isBlank() || kategori.isBlank() || lokasi.isBlank() || deskripsi.isBlank()) {
            Toast.makeText(this, "Form Kosong: Harap isi semua field data wisata", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.tambahWisata(
            namaWisata = nama,
            kategori = kategori,
            lokasi = lokasi,
            hargaTiketText = harga,
            deskripsi = deskripsi,
            foto = foto,
            fotoLokal = fotoTerpilih
        )
    }

    private fun amatiViewModel() {
        viewModel.kondisiSimpan.observe(this) { kondisi ->
            when (kondisi) {
                is UiState.Loading -> {
                    Helper.tampil(pbLoading)
                    btnSimpan.isEnabled = false
                }

                is UiState.Berhasil -> {
                    Helper.sembunyi(pbLoading)
                    btnSimpan.isEnabled = true
                    Toast.makeText(this, kondisi.data, Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }

                is UiState.Gagal -> {
                    Helper.sembunyi(pbLoading)
                    btnSimpan.isEnabled = true
                    Toast.makeText(this, kondisi.pesan, Toast.LENGTH_LONG).show()
                    viewModel.resetKondisi()
                }

                null -> {
                    Helper.sembunyi(pbLoading)
                    btnSimpan.isEnabled = true
                }
            }
        }
    }

    companion object {
        private const val KUNCI_FOTO_TERPILIH = "kunci_foto_terpilih"
    }
}
