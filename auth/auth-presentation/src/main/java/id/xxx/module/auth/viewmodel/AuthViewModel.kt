package id.xxx.module.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.xxx.module.auth.usecase.AuthUseCase

class AuthViewModel(
    private val useCase: AuthUseCase
) : ViewModel() {

    val currentUserAsFlow = useCase.currentUser()

    val currentUserAsLiveData = currentUserAsFlow
        .asLiveData(viewModelScope.coroutineContext)
}