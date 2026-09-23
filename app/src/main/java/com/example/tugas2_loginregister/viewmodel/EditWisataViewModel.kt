package com.example.tugas2_loginregister.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tugas2_loginregister.repository.FavoriteRepository
import com.example.tugas2_loginregister.repository.WisataRepository
import com.example.tugas2_loginregister.utils.UiState
import kotlinx.coroutines.launch

class EditWisataViewModel(aplikasi: Application) : AndroidViewModel(aplikasi) {

    private val repository = WisataRepository(aplikasi)
    private val favoriteRepository = FavoriteRepository(aplikasi)

    private val _kondisiEdit = MutableLiveData<UiState<String>?>()
    val kondisiEdit: LiveData<UiState<String>?> = _kondisiEdit

    fun editWisata(
        id: Int,
        namaWisata: String,
        kategori: String,
        lokasi: String,
        hargaTiketText: String,
        deskripsi: String,
        foto: String,
        fotoLokal: Uri?
    ) {
        val nama = namaWisata.trim()
        val kat = kategori.trim()
        val lok = lokasi.trim()
        val desk = deskripsi.trim()
        val fot = foto.trim()
        val hargaInt = hargaTiketText.trim().toIntOrNull() ?: 0

        if (id <= 0) {
            _kondisiEdit.value = UiState.Gagal("ID wisata tidak valid")
            return
        }

        if (nama.isEmpty() || kat.isEmpty() || lok.isEmpty() || desk.isEmpty()) {
            _kondisiEdit.value = UiState.Gagal("Form tidak boleh ada yang kosong. Harap isi seluruh informasi wisata.")
            return
        }

        _kondisiEdit.value = UiState.Loading

        viewModelScope.launch {
            try {
                val balasan = repository.editWisata(
                    id = id,
                    namaWisata = nama,
                    kategori = kat,
                    lokasi = lok,
                    hargaTiket = hargaInt,
                    deskripsi = desk,
                    foto = fot,
                    fotoLokal = fotoLokal
                )

                if (balasan.success) {
                    // Data favorit di Room ikut disegarkan agar tidak menampilkan data lama
                    balasan.data?.let { favoriteRepository.perbarui(it) }
                    _kondisiEdit.value = UiState.Berhasil(balasan.message.ifBlank { "Data wisata berhasil diperbarui" })
                } else {
                    _kondisiEdit.value = UiState.Gagal(balasan.message.ifBlank { "Gagal memperbarui data wisata" })
                }
            } catch (e: Exception) {
                _kondisiEdit.value = UiState.Gagal("Terjadi kesalahan saat memperbarui: ${e.message}", e)
            }
        }
    }

    fun resetKondisi() {
        _kondisiEdit.value = null
    }
}
