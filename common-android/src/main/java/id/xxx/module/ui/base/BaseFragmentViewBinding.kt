package id.xxx.module.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.delegate.ViewBinding.Companion.getMethodeInflate
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseFragmentViewBinding<T : ViewBinding> : BaseFragment() {

    protected val viewBinding by viewBinding<T>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = viewBinding.root
        if (rootView.background == null)
            rootView.background =
                activity?.window?.decorView?.background
        return rootView
    }

    private fun <T : ViewBinding> viewBinding(
        onDestroy: (T) -> Unit = {},
    ): androidx.fragment.app.delegate.ViewBinding<out T> {
        val types =
            (javaClass.genericSuperclass as? ParameterizedType)
                ?.actualTypeArguments
        types?.forEach { type ->
            @Suppress("UNCHECKED_CAST")
            val classViewBinding = (type as Class<T>)

            val method = classViewBinding.getMethodeInflate()
            if (method != null) {
                return viewBinding(
                    viewBindingClass = classViewBinding,
                    viewGroup = { null },
                    onDestroy = onDestroy,
                )
            }
        }

        throw Throwable("can't create")
    }
}