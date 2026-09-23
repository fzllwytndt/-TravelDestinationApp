package com.example.tugas2_loginregister.ui.activity

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.material.card.MaterialCardView

class EditWisataActivity : AppCompatActivity() {

    private val viewModel: EditWisataViewModel by viewModels()

    private lateinit var btnKembali: ImageButton
    private lateinit var cardFoto: MaterialCardView
    private lateinit var ivPreviewFoto: ImageView
    private lateinit var etNamaWisata: EditText
    private lateinit var etKategori: EditText
    private lateinit var etLokasi: EditText
    private lateinit var etHargaTiket: EditText
    private lateinit var etFoto: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var btnSimpan: MaterialButton
    private lateinit var pbLoading: ProgressBar

    private var idWisata = 0

    /** Foto baru dari galeri. Null berarti foto lama tetap dipakai. */
    private var fotoTerpilih: Uri? = null

    private val pemilihFoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            fotoTerpilih = uri
            tampilkanPratinjau(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_wisata)

        hubungkanView()
        siapkanTombolKembali()
        isiFormAwal()
        pulihkanFotoTerpilih(savedInstanceState)

        cardFoto.setOnClickListener { bukaGaleri() }
        btnSimpan.setOnClickListener { simpanPerubahan() }

        amatiViewModel()
    }

    private fun hubungkanView() {
        btnKembali = findViewById(R.id.btnKembali)
        cardFoto = findViewById(R.id.cardFoto)
        ivPreviewFoto = findViewById(R.id.ivPreviewFoto)
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
            tampilkanFotoLama(wisata.fotoUrl)
        } else {
            idWisata = intent.getIntExtra(KUNCI_ID, 0)
        }
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

    /** Menampilkan foto yang sedang tersimpan di server supaya pengguna tahu apa yang akan diganti. */
    private fun tampilkanFotoLama(fotoUrl: String) {
        if (fotoUrl.isBlank()) {
            return
        }

        Helper.muatGambar(ivPreviewFoto, fotoUrl)
        Helper.tampil(ivPreviewFoto)
    }

    private fun tampilkanPratinjau(uri: Uri) {
        Helper.muatGambar(ivPreviewFoto, uri)
        Helper.tampil(ivPreviewFoto)
    }

    private fun simpanPerubahan() {
        val nama = etNamaWisata.text.toString()
        val kategori = etKategori.text.toString()
        val lokasi = etLokasi.text.toString()
        val harga = etHargaTiket.text.toString()
        val foto = etFoto.text.toString()
        val deskripsi = etDeskripsi.text.toString()

        viewModel.editWisata(
            id = idWisata,
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

        private const val KUNCI_FOTO_TERPILIH = "kunci_foto_terpilih"
    }
}
