@file:JvmName("FragmentActivityKtx")

package id.xxx.module.auth.ktx

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

inline fun <reified T : Fragment> FragmentActivity.getFragment(): T? {
    return supportFragmentManager.fragments.find { it is T } as? T
}

