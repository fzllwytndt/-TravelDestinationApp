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
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var rvWisata: RecyclerView
    private lateinit var pbAwal: ProgressBar
    private lateinit var pbMuatLagi: ProgressBar
    private lateinit var tvPesan: TextView
    private lateinit var tvInfoBawah: TextView

    private lateinit var adapter: WisataAdapter

    private val daftarWisata = mutableListOf<Wisata>()

    private val idSudahAda = mutableSetOf<Int>()

    private var halaman = 1
    private var sedangMemuat = false
    private var semuaSudahDimuat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        siapkanHeader()
        hubungkanView()
        siapkanDaftar()
        pasangScrollListener()

        muatData()
    }

    private fun siapkanHeader() {
        val username = intent.getStringExtra("username")
        findViewById<TextView>(R.id.tvWelcome).text = "Selamat Datang, $username"

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun hubungkanView() {
        rvWisata = findViewById(R.id.rvWisata)
        pbAwal = findViewById(R.id.pbAwal)
        pbMuatLagi = findViewById(R.id.pbMuatLagi)
        tvPesan = findViewById(R.id.tvPesan)
        tvInfoBawah = findViewById(R.id.tvInfoBawah)
    }

    private fun siapkanDaftar() {
        adapter = WisataAdapter(daftarWisata)
        rvWisata.layoutManager = LinearLayoutManager(this)
        rvWisata.adapter = adapter

        tvPesan.setOnClickListener {
            if (!sedangMemuat) {
                muatData()
            }
        }
    }

    private fun pasangScrollListener() {
        rvWisata.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (semuaSudahDimuat) {
                    perbaruiInfoBawah()
                    return
                }

                if (dy <= 0) return
                if (sedangMemuat) return

                if (sudahDekatBawah()) {
                    muatData()
                }
            }
        })
    }

    private fun sudahDekatBawah(): Boolean {
        val layoutManager = rvWisata.layoutManager as LinearLayoutManager
        val posisiTerakhir = layoutManager.findLastVisibleItemPosition()

        return posisiTerakhir >= daftarWisata.size - 3
    }

    private fun muatData() {
        sedangMemuat = true
        tampilkanLoading()

        Thread {
            try {
                val json = JSONObject(ApiClient.get(this, "wisata.php?page=$halaman"))

                val dataBaru = bacaDaftarWisata(json.getJSONArray("data"))
                val totalPage = json.getJSONObject("meta").getInt("total_page")

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

    private fun bacaDaftarWisata(arrayData: JSONArray): List<Wisata> {
        val hasil = mutableListOf<Wisata>()

        for (i in 0 until arrayData.length()) {
            val item = arrayData.getJSONObject(i)

            hasil.add(
                Wisata(
                    id = item.getInt("id"),
                    namaWisata = item.getString("nama_wisata"),
                    deskripsi = item.getString("deskripsi"),
                    fotoUrl = item.getString("foto_url")
                )
            )
        }

        return hasil
    }

    private fun tampilkanLoading() {
        if (daftarWisata.isEmpty()) {
            pbAwal.visibility = View.VISIBLE
            tvPesan.visibility = View.GONE
        } else {
            pbMuatLagi.visibility = View.VISIBLE
        }
    }

    private fun tampilkanData(dataBaru: List<Wisata>, totalPage: Int) {
        sedangMemuat = false
        sembunyikanLoading()

        tambahDataBaru(dataBaru)

        if (daftarWisata.isEmpty()) {
            semuaSudahDimuat = true
            tvPesan.text = "Belum ada data wisata"
            tvPesan.visibility = View.VISIBLE
            return
        }

        tvPesan.visibility = View.GONE

        if (halaman >= totalPage) {
            semuaSudahDimuat = true
            tvInfoBawah.text = "Semua data sudah ditampilkan"

            rvWisata.post { perbaruiInfoBawah() }
        } else {
            halaman++
        }
    }

    private fun tambahDataBaru(dataBaru: List<Wisata>) {
        val posisiAwal = daftarWisata.size

        for (wisata in dataBaru) {
            if (idSudahAda.add(wisata.id)) {
                daftarWisata.add(wisata)
            }
        }

        val jumlahBaru = daftarWisata.size - posisiAwal

        if (jumlahBaru > 0) {
            adapter.notifyItemRangeInserted(posisiAwal, jumlahBaru)
        }
    }

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
        sembunyikanLoading()

        if (daftarWisata.isEmpty()) {
            tvPesan.text = "Gagal memuat data.\nPeriksa koneksi, lalu ketuk di sini untuk mencoba lagi."
            tvPesan.visibility = View.VISIBLE

            DialogServer.tampilkan(this) {
                muatData()
            }
        } else {
            Toast.makeText(this, "Gagal memuat data berikutnya", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sembunyikanLoading() {
        pbAwal.visibility = View.GONE
        pbMuatLagi.visibility = View.GONE
    }
}
