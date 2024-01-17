package id.xxx.module.auth

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.repository.AuthRepositoryImpl
import id.xxx.module.auth.usecase.AuthUseCaseImpl

internal class AuthActivity :
    AuthActivity(AuthUseCaseImpl.getInstance(AuthRepositoryImpl.getInstance())) {

    companion object {
        const val RESULT_USER = AuthActivity.RESULT_USER
    }
}