@file:JvmName("FragmentKtx")

package id.xxx.module.ktx

import androidx.fragment.app.Fragment
import id.xxx.module.autentication.IAuthentication

internal val Fragment.authentication: IAuthentication?
    get() = getCallback()

internal inline fun <reified T> Fragment.getCallback() =
    if (parentFragment is T)
        parentFragment as? T
    else if (activity is T)
        activity as? T
    else
        null