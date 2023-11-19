package id.xxx.module.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.xxx.module.auth.usecase.AuthUseCase

class AuthViewModelProviderFactory(
    private val useCase: AuthUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}