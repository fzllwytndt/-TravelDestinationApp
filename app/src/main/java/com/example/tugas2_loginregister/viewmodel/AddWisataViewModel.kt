package com.example.tugas2_loginregister.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tugas2_loginregister.repository.WisataRepository
import com.example.tugas2_loginregister.utils.UiState
import kotlinx.coroutines.launch

class AddWisataViewModel(aplikasi: Application) : AndroidViewModel(aplikasi) {

    private val repository = WisataRepository(aplikasi)

    private val _kondisiSimpan = MutableLiveData<UiState<String>?>()
    val kondisiSimpan: LiveData<UiState<String>?> = _kondisiSimpan

    fun tambahWisata(
        namaWisata: String,
        kategori: String,
        lokasi: String,
        hargaTiketText: String,
        deskripsi: String,
        foto: String
    ) {
        val nama = namaWisata.trim()
        val kat = kategori.trim()
        val lok = lokasi.trim()
        val desk = deskripsi.trim()
        val fot = foto.trim()
        val hargaInt = hargaTiketText.trim().toIntOrNull() ?: 0

        if (nama.isEmpty() || kat.isEmpty() || lok.isEmpty() || desk.isEmpty()) {
            _kondisiSimpan.value = UiState.Gagal("Form tidak boleh ada yang kosong. Harap isi seluruh informasi wisata.")
            return
        }

        _kondisiSimpan.value = UiState.Loading

        viewModelScope.launch {
            try {
                val balasan = repository.tambahWisata(
                    namaWisata = nama,
                    kategori = kat,
                    lokasi = lok,
                    hargaTiket = hargaInt,
                    deskripsi = desk,
                    foto = fot
                )

                if (balasan.success) {
                    _kondisiSimpan.value = UiState.Berhasil(balasan.message.ifBlank { "Data wisata berhasil ditambahkan" })
                } else {
                    _kondisiSimpan.value = UiState.Gagal(balasan.message.ifBlank { "Gagal menambahkan data wisata" })
                }
            } catch (e: Exception) {
                _kondisiSimpan.value = UiState.Gagal("Terjadi kesalahan saat menyimpan: ${e.message}", e)
            }
        }
    }

    fun resetKondisi() {
        _kondisiSimpan.value = null
    }
}
