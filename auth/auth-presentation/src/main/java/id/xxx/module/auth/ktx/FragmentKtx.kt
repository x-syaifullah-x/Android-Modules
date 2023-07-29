@file:JvmName("FragmentKtx")

package id.xxx.module.auth.ktx

import androidx.fragment.app.Fragment

inline fun <reified T> Fragment.getListener(): T? {
    val result =
        if (parentFragment is T)
            parentFragment
        else if (activity is T)
            activity
        else
            null
    return result as? T
}

