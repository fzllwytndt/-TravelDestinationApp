package com.example.tugas2_loginregister.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.utils.Helper
import com.example.tugas2_loginregister.utils.UiState
import com.example.tugas2_loginregister.viewmodel.AddWisataViewModel
import com.google.android.material.button.MaterialButton

class AddWisataActivity : AppCompatActivity() {

    private val viewModel: AddWisataViewModel by viewModels()

    private lateinit var btnKembali: ImageButton
    private lateinit var ivPreviewFoto: ImageView
    private lateinit var wadahPlaceholderFoto: View
    private lateinit var etNamaWisata: EditText
    private lateinit var etKategori: EditText
    private lateinit var etLokasi: EditText
    private lateinit var etHargaTiket: EditText
    private lateinit var etFoto: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var btnSimpan: MaterialButton
    private lateinit var pbLoading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wisata)

        hubungkanView()
        siapkanTombolKembali()

        btnSimpan.setOnClickListener { simpanData() }

        amatiViewModel()
    }

    private fun hubungkanView() {
        btnKembali = findViewById(R.id.btnKembali)
        ivPreviewFoto = findViewById(R.id.ivPreviewFoto)
        wadahPlaceholderFoto = findViewById(R.id.wadahPlaceholderFoto)
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
            foto = foto
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
}
