package com.example.tugas2_loginregister

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    private lateinit var isiDetail: View
    private lateinit var ivFoto: ImageView
    private lateinit var tvKategori: TextView
    private lateinit var tvNama: TextView
    private lateinit var tvLokasi: TextView
    private lateinit var tvHarga: TextView
    private lateinit var tvDeskripsi: TextView
    private lateinit var pbLoading: ProgressBar
    private lateinit var tvPesan: TextView

    private var idWisata = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        hubungkanView()

        idWisata = intent.getIntExtra("id", 0)

        tvPesan.setOnClickListener { muatDetail() }

        muatDetail()
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
    }

    private fun muatDetail() {
        Helper.sembunyi(isiDetail, tvPesan)
        Helper.tampil(pbLoading)

        Helper.ambilDataApi(
            activity = this,
            alamat = "wisata_detail.php?id=$idWisata",
            saatBerhasil = { json -> tampilkanDetail(json) },
            saatGagal = {
                tampilkanPesan("Gagal memuat detail wisata.\nKetuk di sini untuk mencoba lagi.")
            }
        )
    }

    private fun tampilkanDetail(json: JSONObject) {
        Helper.sembunyi(pbLoading)

        if (!json.getBoolean("success")) {
            tampilkanPesan(json.getString("message"))
            return
        }

        val wisata = Helper.bacaWisata(json.getJSONObject("data"))

        Helper.muatGambar(ivFoto, wisata.fotoUrl)

        tvKategori.text = wisata.kategori
        tvNama.text = wisata.namaWisata
        tvLokasi.text = wisata.lokasi
        tvHarga.text = Helper.rupiah(wisata.hargaTiket)
        tvDeskripsi.text = wisata.deskripsi

        Helper.tampil(isiDetail)
    }

    private fun tampilkanPesan(teks: String) {
        Helper.sembunyi(pbLoading, isiDetail)

        tvPesan.text = teks
        Helper.tampil(tvPesan)
    }
}
