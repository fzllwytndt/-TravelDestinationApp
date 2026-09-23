package com.example.tugas2_loginregister.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tugas2_loginregister.R
import com.example.tugas2_loginregister.model.Wisata
import com.example.tugas2_loginregister.utils.Helper
import com.example.tugas2_loginregister.utils.UiState
import com.example.tugas2_loginregister.viewmodel.EditWisataViewModel
import com.google.android.material.button.MaterialButton

class EditWisataActivity : AppCompatActivity() {

    private val viewModel: EditWisataViewModel by viewModels()

    private lateinit var btnKembali: ImageButton
    private lateinit var etNamaWisata: EditText
    private lateinit var etKategori: EditText
    private lateinit var etLokasi: EditText
    private lateinit var etHargaTiket: EditText
    private lateinit var etFoto: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var btnSimpan: MaterialButton
    private lateinit var pbLoading: ProgressBar

    private var idWisata = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_wisata)

        hubungkanView()
        siapkanTombolKembali()
        isiFormAwal()

        btnSimpan.setOnClickListener { simpanPerubahan() }

        amatiViewModel()
    }

    private fun hubungkanView() {
        btnKembali = findViewById(R.id.btnKembali)
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
        val headerEdit = findViewById<View>(R.id.headerEdit)
        ViewCompat.setOnApplyWindowInsetsListener(headerEdit) { view, jarakSistem ->
            val atas = jarakSistem.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, atas, 0, 0)
            jarakSistem
        }

        btnKembali.setOnClickListener { finish() }
    }

    @Suppress("DEPRECATION")
    private fun isiFormAwal() {
        val wisata = intent.getSerializableExtra(KUNCI_WISATA) as? Wisata

        if (wisata != null) {
            idWisata = wisata.id
            etNamaWisata.setText(wisata.namaWisata)
            etKategori.setText(wisata.kategori)
            etLokasi.setText(wisata.lokasi)
            etHargaTiket.setText(wisata.hargaTiket.toString())
            etFoto.setText(wisata.foto.ifBlank { wisata.fotoUrl })
            etDeskripsi.setText(wisata.deskripsi)
        } else {
            idWisata = intent.getIntExtra(KUNCI_ID, 0)
        }
    }

    private fun simpanPerubahan() {
        val nama = etNamaWisata.text.toString()
        val kategori = etKategori.text.toString()
        val lokasi = etLokasi.text.toString()
        val harga = etHargaTiket.text.toString()
        val foto = etFoto.text.toString()
        val deskripsi = etDeskripsi.text.toString()

        if (nama.isBlank() || kategori.isBlank() || lokasi.isBlank() || deskripsi.isBlank()) {
            Toast.makeText(this, "Form Kosong: Harap isi semua field wajib", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.editWisata(
            id = idWisata,
            namaWisata = nama,
            kategori = kategori,
            lokasi = lokasi,
            hargaTiketText = harga,
            deskripsi = deskripsi,
            foto = foto
        )
    }

    private fun amatiViewModel() {
        viewModel.kondisiEdit.observe(this) { kondisi ->
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
        const val KUNCI_WISATA = "kunci_wisata"
        const val KUNCI_ID = "kunci_id"
    }
}
