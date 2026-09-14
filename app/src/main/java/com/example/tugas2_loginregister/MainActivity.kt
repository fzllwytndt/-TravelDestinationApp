package com.example.tugas2_loginregister

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var svCari: SearchView
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

    private var kataKunci = ""

    private var tokenPermintaan = 0

    private val handlerCari = Handler(Looper.getMainLooper())
    private var tundaCari: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        siapkanHeader()
        hubungkanView()
        siapkanDaftar()
        siapkanPencarian()
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
        svCari = findViewById(R.id.svCari)
        rvWisata = findViewById(R.id.rvWisata)
        pbAwal = findViewById(R.id.pbAwal)
        pbMuatLagi = findViewById(R.id.pbMuatLagi)
        tvPesan = findViewById(R.id.tvPesan)
        tvInfoBawah = findViewById(R.id.tvInfoBawah)
    }

    private fun siapkanDaftar() {
        adapter = WisataAdapter(daftarWisata) { wisata -> bukaDetail(wisata) }

        rvWisata.layoutManager = LinearLayoutManager(this)
        rvWisata.adapter = adapter

        tvPesan.setOnClickListener {
            if (!sedangMemuat && !semuaSudahDimuat) {
                muatData()
            }
        }
    }

    private fun bukaDetail(wisata: Wisata) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("id", wisata.id)
        startActivity(intent)
    }

    private fun siapkanPencarian() {
        svCari.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                batalkanTundaCari()
                svCari.clearFocus()
                mulaiPencarian(query.orEmpty())
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

        val tugas = Runnable { mulaiPencarian(kunci) }

        tundaCari = tugas
        handlerCari.postDelayed(tugas, JEDA_KETIK)
    }

    private fun batalkanTundaCari() {
        tundaCari?.let { handlerCari.removeCallbacks(it) }
        tundaCari = null
    }

    private fun mulaiPencarian(kunci: String) {
        val kunciBaru = kunci.trim()

        if (kunciBaru == kataKunci) return

        kataKunci = kunciBaru

        kosongkanDaftar()
        muatData()
    }

    private fun kosongkanDaftar() {
        tokenPermintaan++

        val jumlahLama = daftarWisata.size

        daftarWisata.clear()
        idSudahAda.clear()

        if (jumlahLama > 0) {
            adapter.notifyItemRangeRemoved(0, jumlahLama)
        }

        halaman = 1
        sedangMemuat = false
        semuaSudahDimuat = false

        Helper.sembunyi(tvPesan, tvInfoBawah)
        sembunyikanLoading()
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

        val token = tokenPermintaan
        val alamat = "wisata.php?page=$halaman&q=" + Helper.sandikan(kataKunci)

        Helper.ambilDataApi(
            activity = this,
            alamat = alamat,
            saatBerhasil = { json ->
                if (token == tokenPermintaan) {
                    bacaHasilApi(json)
                }
            },
            saatGagal = {
                if (token == tokenPermintaan) {
                    tampilkanError()
                }
            }
        )
    }

    private fun bacaHasilApi(json: JSONObject) {
        val dataBaru = bacaDaftarWisata(json.getJSONArray("data"))
        val totalPage = json.getJSONObject("meta").getInt("total_page")

        tampilkanData(dataBaru, totalPage)
    }

    private fun bacaDaftarWisata(arrayData: JSONArray): List<Wisata> {
        val hasil = mutableListOf<Wisata>()

        for (i in 0 until arrayData.length()) {
            hasil.add(Helper.bacaWisata(arrayData.getJSONObject(i)))
        }

        return hasil
    }

    private fun tampilkanLoading() {
        if (daftarWisata.isEmpty()) {
            Helper.tampil(pbAwal)
            Helper.sembunyi(tvPesan)
        } else {
            Helper.tampil(pbMuatLagi)
        }
    }

    private fun tampilkanData(dataBaru: List<Wisata>, totalPage: Int) {
        sedangMemuat = false
        sembunyikanLoading()

        tambahDataBaru(dataBaru)

        if (daftarWisata.isEmpty()) {
            semuaSudahDimuat = true

            tvPesan.text =
                if (kataKunci.isBlank()) "Belum ada data wisata"
                else "Wisata \"$kataKunci\" tidak ditemukan"

            Helper.tampil(tvPesan)
            return
        }

        Helper.sembunyi(tvPesan)

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
            Helper.sembunyi(tvInfoBawah)
            return
        }

        val layoutManager = rvWisata.layoutManager as LinearLayoutManager
        val posisiTerakhir = layoutManager.findLastVisibleItemPosition()

        Helper.tampilJika(tvInfoBawah, posisiTerakhir >= daftarWisata.size - 1)
    }

    private fun tampilkanError() {
        sedangMemuat = false
        sembunyikanLoading()

        if (daftarWisata.isEmpty()) {
            tvPesan.text = "Gagal memuat data.\nPeriksa koneksi, lalu ketuk di sini untuk mencoba lagi."
            Helper.tampil(tvPesan)

            if (kataKunci.isBlank()) {
                DialogServer.tampilkan(this) {
                    muatData()
                }
            }
        } else {
            Helper.pesanSingkat(this, "Gagal memuat data berikutnya")
        }
    }

    private fun sembunyikanLoading() {
        Helper.sembunyi(pbAwal, pbMuatLagi)
    }

    override fun onDestroy() {
        super.onDestroy()
        batalkanTundaCari()
    }

    companion object {
        private const val JEDA_KETIK = 400L
    }
}
