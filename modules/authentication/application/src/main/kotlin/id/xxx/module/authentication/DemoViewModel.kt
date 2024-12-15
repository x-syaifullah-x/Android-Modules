package id.xxx.module.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import id.xxx.module.auth.data.AuthRepositoryImpl
import id.xxx.module.auth.data.source.local.LocalDataSource
import id.xxx.module.auth.data.source.local.db.AppDatabase
import id.xxx.module.auth.data.source.remote.RemoteDataSource
import id.xxx.module.auth.domain.model.AuthenticationType
import id.xxx.module.auth.domain.usecase.AuthUseCase
import id.xxx.module.auth.domain.usecase.AuthUseCaseImpl

class DemoViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
//                val savedStateHandle = extras.createSavedStateHandle()
                val db = AppDatabase.getInstance(application)
                val authRepository = AuthRepositoryImpl.getInstance(
                    local = LocalDataSource.getInstance(dao = db.userDao),
                    remote = RemoteDataSource.getInstance(),
                )
                val authUseCase = AuthUseCaseImpl.getInstance(authRepository)
                return DemoViewModel(authUseCase) as T
            }
        }
    }

    fun authentication(authType: AuthenticationType) = authUseCase.sign(authType)
}