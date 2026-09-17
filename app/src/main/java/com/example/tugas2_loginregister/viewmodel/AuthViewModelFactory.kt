package com.example.tugas2_loginregister.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tugas2_loginregister.repository.AuthRepository

/** [AuthViewModel] membutuhkan Repository, jadi pembuatannya diatur di sini. */
class AuthViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val repository = AuthRepository(context.applicationContext)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }
}
