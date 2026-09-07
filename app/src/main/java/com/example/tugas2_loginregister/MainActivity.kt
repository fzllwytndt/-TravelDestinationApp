package com.example.tugas2_loginregister

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var rvWisata: RecyclerView
    private lateinit var pbAwal: ProgressBar
    private lateinit var pbMuatLagi: ProgressBar
    private lateinit var tvPesan: TextView
    private lateinit var tvInfoBawah: TextView

    private lateinit var adapter: WisataAdapter

    // Semua data yang sedang ditampilkan di layar.
    private val daftarWisata = mutableListOf<Wisata>()

    // Menampung id yang sudah pernah masuk daftar.
    // Ini yang dipakai untuk mencegah data tampil dobel.
    private val idSudahAda = mutableSetOf<Int>()

    private var halaman = 1              // halaman yang akan diminta berikutnya
    private var sedangMemuat = false     // penanda supaya tidak meminta data dua kali sekaligus
    private var semuaSudahDimuat = false // penanda kalau seluruh data sudah terambil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = intent.getStringExtra("username")
        findViewById<TextView>(R.id.tvWelcome).text = "Selamat Datang, $username"

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        rvWisata = findViewById(R.id.rvWisata)
        pbAwal = findViewById(R.id.pbAwal)
        pbMuatLagi = findViewById(R.id.pbMuatLagi)
        tvPesan = findViewById(R.id.tvPesan)
        tvInfoBawah = findViewById(R.id.tvInfoBawah)

        adapter = WisataAdapter(daftarWisata)
        rvWisata.layoutManager = LinearLayoutManager(this)
        rvWisata.adapter = adapter

        // Kalau gagal memuat, pesannya bisa ditekan untuk mencoba lagi.
        tvPesan.setOnClickListener {
            if (!sedangMemuat) {
                muatData()
            }
        }

        pasangScrollListener()

        // Pemuatan pertama, halaman 1.
        muatData()
    }

    // Mendeteksi saat pengguna menggulir mendekati bagian bawah daftar,
    // lalu meminta 10 data berikutnya secara otomatis.
    private fun pasangScrollListener() {
        rvWisata.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Data sudah habis, tinggal mengatur kapan keterangannya tampil.
                if (semuaSudahDimuat) {
                    perbaruiInfoBawah()
                    return
                }

                if (dy <= 0) return              // hanya bereaksi saat menggulir ke bawah
                if (sedangMemuat) return         // masih ada permintaan yang berjalan

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val posisiTerakhir = layoutManager.findLastVisibleItemPosition()

                // Dimuat saat tersisa 3 item lagi menuju bawah, supaya
                // data berikutnya sudah siap sebelum pengguna sampai ujung.
                if (posisiTerakhir >= daftarWisata.size - 3) {
                    muatData()
                }
            }
        })
    }

    private fun muatData() {
        sedangMemuat = true
        tampilkanLoading()

        // Permintaan jaringan tidak boleh dijalankan di thread utama,
        // jadi dikerjakan di Thread terpisah seperti pada halaman login.
        Thread {
            try {
                val hasil = ApiClient.get("wisata.php?page=$halaman")
                val json = JSONObject(hasil)

                val arrayData = json.getJSONArray("data")
                val totalPage = json.getJSONObject("meta").getInt("total_page")

                // Ubah JSON menjadi daftar objek Wisata.
                val dataBaru = mutableListOf<Wisata>()

                for (i in 0 until arrayData.length()) {
                    val item = arrayData.getJSONObject(i)

                    dataBaru.add(
                        Wisata(
                            id = item.getInt("id"),
                            namaWisata = item.getString("nama_wisata"),
                            deskripsi = item.getString("deskripsi"),
                            fotoUrl = item.getString("foto_url")
                        )
                    )
                }

                runOnUiThread {
                    tampilkanData(dataBaru, totalPage)
                }

            } catch (e: Exception) {
                runOnUiThread {
                    tampilkanError()
                }
            }
        }.start()
    }

    private fun tampilkanLoading() {
        if (daftarWisata.isEmpty()) {
            // Pemuatan pertama: loading besar di tengah layar.
            pbAwal.visibility = View.VISIBLE
            tvPesan.visibility = View.GONE
        } else {
            // Pemuatan berikutnya: loading kecil di bawah daftar.
            pbMuatLagi.visibility = View.VISIBLE
        }
    }

    private fun tampilkanData(dataBaru: List<Wisata>, totalPage: Int) {
        sedangMemuat = false
        pbAwal.visibility = View.GONE
        pbMuatLagi.visibility = View.GONE

        val posisiAwal = daftarWisata.size

        // idSudahAda.add() menghasilkan false kalau id-nya sudah pernah masuk,
        // sehingga data yang sama tidak akan ditambahkan dua kali.
        for (wisata in dataBaru) {
            if (idSudahAda.add(wisata.id)) {
                daftarWisata.add(wisata)
            }
        }

        val jumlahBaru = daftarWisata.size - posisiAwal

        if (jumlahBaru > 0) {
            adapter.notifyItemRangeInserted(posisiAwal, jumlahBaru)
        }

        // Kondisi data kosong.
        if (daftarWisata.isEmpty()) {
            semuaSudahDimuat = true
            tvPesan.text = "Belum ada data wisata"
            tvPesan.visibility = View.VISIBLE
            return
        }

        tvPesan.visibility = View.GONE

        if (halaman >= totalPage) {
            // Halaman terakhir sudah terambil, pemuatan dihentikan.
            semuaSudahDimuat = true
            tvInfoBawah.text = "Semua data sudah ditampilkan"

            // post() dipakai supaya pengecekan dilakukan setelah daftar
            // selesai digambar, sehingga posisi item terakhir sudah benar.
            rvWisata.post { perbaruiInfoBawah() }
        } else {
            // Siapkan nomor halaman untuk permintaan berikutnya.
            halaman++
        }
    }

    // Keterangan "semua data sudah ditampilkan" hanya dimunculkan ketika
    // pengguna benar-benar sampai di kartu terakhir, bukan saat masih di tengah daftar.
    private fun perbaruiInfoBawah() {
        if (!semuaSudahDimuat) {
            tvInfoBawah.visibility = View.GONE
            return
        }

        val layoutManager = rvWisata.layoutManager as LinearLayoutManager
        val posisiTerakhir = layoutManager.findLastVisibleItemPosition()

        tvInfoBawah.visibility =
            if (posisiTerakhir >= daftarWisata.size - 1) View.VISIBLE else View.GONE
    }

    private fun tampilkanError() {
        sedangMemuat = false
        pbAwal.visibility = View.GONE
        pbMuatLagi.visibility = View.GONE

        if (daftarWisata.isEmpty()) {
            // Gagal sejak awal, tampilkan pesan di tengah layar.
            tvPesan.text = "Gagal memuat data.\nPeriksa koneksi, lalu ketuk di sini untuk mencoba lagi."
            tvPesan.visibility = View.VISIBLE
        } else {
            // Sudah ada data di layar, cukup beri tahu lewat Toast.
            Toast.makeText(this, "Gagal memuat data berikutnya", Toast.LENGTH_SHORT).show()
        }
    }
}
