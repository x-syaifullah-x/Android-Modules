@file:JvmName("ContextKtx")

package id.xxx.module.auth.ktx

import android.content.Context
import android.content.res.Configuration

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}
