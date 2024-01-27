@file:JvmName("FragmentKtx")

package id.xxx.module.auth.ktx

import android.view.View
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

fun Fragment.hideSoftInputFromWindow(v: View? = null, flag: Int = 0) =
    context?.hideSoftInputFromWindow(iBinder = (v ?: view)?.windowToken, flag = flag)


