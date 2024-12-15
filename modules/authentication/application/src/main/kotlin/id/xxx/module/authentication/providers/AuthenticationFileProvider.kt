package id.xxx.module.authentication.providers

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import id.xxx.modules.authentication.application.BuildConfig

class AuthenticationFileProvider : FileProvider() {

    companion object {

        private const val AUTHORITIES = "${BuildConfig.APPLICATION_ID}.FILE_PROVIDER"

        fun getUriForFile(context: Context, file: File): Uri {
            return getUriForFile(
                context, AUTHORITIES, file
            )
        }
    }
}