package com.example.tugas2_loginregister.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tugas2_loginregister.model.Wisata
import com.example.tugas2_loginregister.repository.FavoriteRepository
import com.example.tugas2_loginregister.repository.WisataRepository
import com.example.tugas2_loginregister.utils.PesanGagal
import com.example.tugas2_loginregister.utils.UiState
import kotlinx.coroutines.launch

/** Mengurus isi halaman Detail Wisata, tombol Like/Unlike, dan hapus wisata. */
class DetailWisataViewModel(aplikasi: Application) : AndroidViewModel(aplikasi) {

    private val wisataRepository = WisataRepository(aplikasi)
    private val favoriteRepository = FavoriteRepository(aplikasi)

    private val _kondisi = MutableLiveData<UiState<Wisata>>()
    val kondisi: LiveData<UiState<Wisata>> = _kondisi

    private val _kondisiHapus = MutableLiveData<UiState<String>?>()
    val kondisiHapus: LiveData<UiState<String>?> = _kondisiHapus

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

    /** Hapus data wisata dari backend API dan Room Database jika ada. */
    fun hapusWisata(id: Int) {
        _kondisiHapus.value = UiState.Loading

        viewModelScope.launch {
            try {
                val balasan = wisataRepository.hapusWisata(id)
                if (balasan.success) {
                    // Hapus dari Room Database favorit jika sebelumnya di-favorite
                    favoriteRepository.hapus(id)
                    _kondisiHapus.value = UiState.Berhasil(balasan.message.ifBlank { "Data wisata berhasil dihapus" })
                } else {
                    _kondisiHapus.value = UiState.Gagal(balasan.message.ifBlank { "Gagal menghapus data wisata" })
                }
            } catch (e: Exception) {
                _kondisiHapus.value = UiState.Gagal(PesanGagal.dari("menghapus data wisata", e), e)
            }
        }
    }

    fun resetKondisiHapus() {
        _kondisiHapus.value = null
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
