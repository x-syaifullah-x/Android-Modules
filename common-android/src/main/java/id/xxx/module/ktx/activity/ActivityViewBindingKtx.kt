@file:JvmName("ActivityViewBindingKtx")

package id.xxx.module.ktx.activity

import android.app.Activity
import android.view.LayoutInflater
import androidx.fragment.app.delegate.ViewBinding.Companion.getMethodeInflate
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
internal fun <T : ViewBinding> Activity.viewBinding(): Lazy<T> {
    val types =
        (javaClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments
    types?.forEach { type ->
        val classViewBinding = (type as Class<T>)
        val method = classViewBinding.getMethodeInflate()
        if (method != null) {
            return viewBinding { layoutInflater ->
                method.invoke(null, layoutInflater, null, false) as T
            }
        }
    }

    throw Throwable("please add generic type extend ViewBinding")
}

inline fun <T : ViewBinding> Activity.viewBinding(
    crossinline inflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    inflater(layoutInflater)
}