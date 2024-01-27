@file:JvmName("ContextKtx")

package id.xxx.module.auth.ktx

import android.content.Context
import android.content.res.Configuration
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Context.hideSoftInputFromWindow(iBinder: IBinder? = null, flag: Int = 0) =
    getInputMethodManager()?.hideSoftInputFromWindow(iBinder, flag)

fun Context.getInputMethodManager() =
    ContextCompat.getSystemService(this, InputMethodManager::class.java)