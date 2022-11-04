@file:JvmName("FragmentViewBindingKtx")

package id.xxx.module.ktx.fragment

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

inline fun <reified T : ViewBinding> Fragment.viewBinding(
    noinline viewGroup: () -> ViewGroup? = { null },
    noinline onDestroy: (T) -> Unit = { }
) = androidx.fragment.app.delegate.ViewBinding(
    viewBindingClass = T::class.java,
    viewGroup = viewGroup,
    onDestroy = onDestroy
)