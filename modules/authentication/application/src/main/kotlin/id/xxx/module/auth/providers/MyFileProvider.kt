package id.xxx.module.auth.providers

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import id.xxx.module.auth.BuildConfig
import java.io.File

class MyFileProvider : FileProvider() {

    companion object {

        private const val AUTHORITIES = "${BuildConfig.APPLICATION_ID}.FILE_PROVIDER"

        fun getUriForFile(context: Context, file: File): Uri {
            return getUriForFile(
                context, AUTHORITIES, file
            )
        }
    }
}