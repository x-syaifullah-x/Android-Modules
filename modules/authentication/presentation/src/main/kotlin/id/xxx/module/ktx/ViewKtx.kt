@file:JvmName("ViewKtx")

package id.xxx.module.ktx

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService

fun View.hideSoftInput(): Boolean? {
    val imm = context.getSystemService<InputMethodManager>()
    return imm?.hideSoftInputFromWindow(windowToken, 0)
}