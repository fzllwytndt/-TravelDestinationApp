package com.example.tugas2_loginregister.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tugas2_loginregister.data.local.room.FavoriteWisata
import com.example.tugas2_loginregister.repository.FavoriteRepository
import kotlinx.coroutines.launch

/** Menyediakan daftar wisata favorit untuk Fragment Favorit. */
class FavoriteViewModel(aplikasi: Application) : AndroidViewModel(aplikasi) {

    private val repository = FavoriteRepository(aplikasi)

    /** Isinya langsung berubah setiap ada data favorit yang ditambah atau dihapus. */
    val daftarFavorit: LiveData<List<FavoriteWisata>> = repository.ambilSemua()

    fun hapus(id: Int) {
        viewModelScope.launch {
            repository.hapus(id)
        }
    }
}
