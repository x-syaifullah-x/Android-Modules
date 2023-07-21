package id.xxx.module.auth

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.repository.AuthRepositoryImpl
import id.xxx.module.auth.usecase.AuthUseCaseImpl

open class MainActivity :
    AuthActivity(AuthUseCaseImpl.getInstance(AuthRepositoryImpl.getInstance())) {

    companion object {
        const val RESULT_USER = AuthActivity.RESULT_USER
    }
}