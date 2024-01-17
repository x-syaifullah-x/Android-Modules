@file:JvmName("FragmentKtx")

package id.xxx.module.auth.ktx

import androidx.fragment.app.Fragment

inline fun <reified Listener> Fragment.getListener(): Listener? {
    val result =
        if (parentFragment is Listener)
            parentFragment
        else if (activity is Listener)
            activity
        else
            null
    return result as? Listener
}

fun Fragment.getInputMethodManager() =
    context?.getInputMethodManager()

