package id.xxx.module.fragment.base

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

abstract class BaseFragment(
    @LayoutRes contentLayoutId: Int = 0
) : Fragment(contentLayoutId) {

    protected fun <T : ViewBinding> viewBinding(
        viewBindingClass: KClass<T>,
        viewGroup: () -> ViewGroup? = { null },
        onDestroy: (T) -> Unit = { },
    ) = viewBinding(
        viewBindingClass = viewBindingClass.java,
        viewGroup = viewGroup,
        onDestroy = onDestroy
    )

    protected fun <T : ViewBinding> viewBinding(
        viewBindingClass: Class<T>,
        viewGroup: () -> ViewGroup? = { null },
        onDestroy: (T) -> Unit = { },
    ) = androidx.fragment.app.delegate.ViewBinding(
        viewBindingClass = viewBindingClass,
        viewGroup = viewGroup,
        onDestroy = onDestroy
    )
}