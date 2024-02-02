package id.xxx.example.room.providers

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import id.xxx.example.room.BuildConfig
import java.io.File

class MyFileProvider : FileProvider() {

    companion object {

        private const val AUTHORITIES = "${BuildConfig.APPLICATION_ID}.FILE_PROVIDER"

        fun getUriForFile(context: Context, file: File): Uri {
            return FileProvider.getUriForFile(
                context, AUTHORITIES, file
            )
        }
    }
}