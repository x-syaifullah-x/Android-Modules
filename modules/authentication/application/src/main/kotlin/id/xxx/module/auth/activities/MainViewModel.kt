package id.xxx.module.auth.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import id.xxx.module.auth.data.AuthRepositoryImpl
import id.xxx.module.auth.data.source.local.LocalDataSource
import id.xxx.module.auth.data.source.local.db.AppDatabase
import id.xxx.module.auth.data.source.remote.RemoteDataSource
import id.xxx.module.auth.domain.model.TypeSign
import id.xxx.module.auth.domain.usecase.AuthUseCase
import id.xxx.module.auth.domain.usecase.AuthUseCaseImpl

class MainViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    companion object {

        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[AndroidViewModelFactory.APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()
                println("MainViewModel: $savedStateHandle")
                val db = AppDatabase.getInstance(application)
                val authRepository =
                    AuthRepositoryImpl.getInstance(
                        local = LocalDataSource.getInstance(dao = db.userDao),
                        remote = RemoteDataSource.getInstance(),
                    )
                val authUseCaseRepository =
                    AuthUseCaseImpl.getInstance(authRepository)
                return MainViewModel(authUseCaseRepository) as T
            }
        }
    }

    val currentUser = authUseCase.getCurrentUser()
        .asLiveData(context = viewModelScope.coroutineContext)

    fun sign(type: TypeSign) =
        authUseCase.sign(type)
}