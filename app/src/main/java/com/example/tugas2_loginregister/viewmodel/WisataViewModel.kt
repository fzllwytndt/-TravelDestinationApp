package com.example.tugas2_loginregister.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tugas2_loginregister.model.Wisata
import com.example.tugas2_loginregister.repository.WisataRepository
import com.example.tugas2_loginregister.utils.UiState
import kotlinx.coroutines.launch

/** Mengurus daftar wisata pada Fragment Home, lengkap dengan pencarian dan pagination. */
class WisataViewModel(aplikasi: Application) : AndroidViewModel(aplikasi) {

    private val repository = WisataRepository(aplikasi)

    private val daftar = mutableListOf<Wisata>()
    private val idSudahAda = mutableSetOf<Int>()

    private val _kondisi = MutableLiveData<UiState<List<Wisata>>>()
    val kondisi: LiveData<UiState<List<Wisata>>> = _kondisi

    private val _semuaSudahDimuat = MutableLiveData(false)
    val semuaSudahDimuat: LiveData<Boolean> = _semuaSudahDimuat

    var kataKunci = ""
        private set

    private var halaman = 1
    private var sedangMemuat = false

    val jumlahData: Int
        get() = daftar.size

    /** Dipanggil sekali saat halaman dibuka supaya data tidak diambil ulang ketika layar diputar. */
    fun muatPertamaKali() {
        if (daftar.isEmpty() && !sedangMemuat) {
            muatData()
        }
    }

    fun cari(kunci: String) {
        val kunciBaru = kunci.trim()

        if (kunciBaru == kataKunci) {
            return
        }

        kataKunci = kunciBaru
        kosongkanDaftar()
        muatData()
    }

    /** Dipanggil ketika gulungan daftar sudah mendekati data terakhir. */
    fun muatBerikutnya() {
        if (sedangMemuat || _semuaSudahDimuat.value == true) {
            return
        }

        muatData()
    }

    /** Dipakai tombol coba lagi ketika pengambilan data gagal. */
    fun muatUlang() {
        if (!sedangMemuat) {
            muatData()
        }
    }

    private fun kosongkanDaftar() {
        daftar.clear()
        idSudahAda.clear()

        halaman = 1
        _semuaSudahDimuat.value = false
    }

    private fun muatData() {
        sedangMemuat = true
        _kondisi.value = UiState.Loading

        viewModelScope.launch {
            try {
                val balasan = repository.ambilDaftarWisata(halaman, kataKunci)

                tampungData(balasan.data)

                if (halaman >= balasan.meta.totalPage) {
                    _semuaSudahDimuat.value = true
                } else {
                    halaman++
                }

                _kondisi.value = UiState.Berhasil(daftar.toList())

            } catch (e: Exception) {
                _kondisi.value = UiState.Gagal("Gagal memuat data wisata.", e)
            }

            sedangMemuat = false
        }
    }

    /** Data dengan id yang sama tidak ditambahkan dua kali. */
    private fun tampungData(dataBaru: List<Wisata>) {
        for (wisata in dataBaru) {
            if (idSudahAda.add(wisata.id)) {
                daftar.add(wisata)
            }
        }
    }
}
