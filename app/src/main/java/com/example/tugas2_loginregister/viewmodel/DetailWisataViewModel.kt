package com.example.tugas2_loginregister.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tugas2_loginregister.model.Wisata
import com.example.tugas2_loginregister.repository.FavoriteRepository
import com.example.tugas2_loginregister.repository.WisataRepository
import com.example.tugas2_loginregister.utils.UiState
import kotlinx.coroutines.launch

/** Mengurus isi halaman Detail Wisata sekaligus tombol Like/Unlike. */
class DetailWisataViewModel(aplikasi: Application) : AndroidViewModel(aplikasi) {

    private val wisataRepository = WisataRepository(aplikasi)
    private val favoriteRepository = FavoriteRepository(aplikasi)

    private val _kondisi = MutableLiveData<UiState<Wisata>>()
    val kondisi: LiveData<UiState<Wisata>> = _kondisi

    fun muatDetail(id: Int) {
        _kondisi.value = UiState.Loading

        viewModelScope.launch {
            try {
                val balasan = wisataRepository.ambilDetailWisata(id)
                val wisata = balasan.data

                _kondisi.value =
                    if (balasan.success && wisata != null) UiState.Berhasil(wisata)
                    else UiState.Gagal(balasan.message.ifBlank { "Data wisata tidak ditemukan" })

            } catch (e: Exception) {
                _kondisi.value = UiState.Gagal(
                    "Gagal memuat detail wisata.\nKetuk di sini untuk mencoba lagi.", e
                )
            }
        }
    }

    /** Dipakai halaman Detail untuk menentukan warna ikon love. */
    fun statusFavorit(id: Int): LiveData<Boolean> {
        return favoriteRepository.cekFavorit(id)
    }

    /** Satu tombol untuk dua keadaan: Like menyimpan data, Unlike menghapus data. */
    fun ubahFavorit(wisata: Wisata, sudahFavorit: Boolean) {
        viewModelScope.launch {
            if (sudahFavorit) {
                favoriteRepository.hapus(wisata.id)
            } else {
                favoriteRepository.simpan(wisata)
            }
        }
    }
}
