package id.xxx.module.auth

import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.repository.AuthRepositoryImpl
import id.xxx.module.auth.usecase.AuthUseCaseImpl

class AuthActivity :
    AuthActivity(AuthUseCaseImpl.getInstance(AuthRepositoryImpl.getInstance())) {

    companion object {
        const val RESULT_USER = AuthActivity.RESULT_USER
    }
}