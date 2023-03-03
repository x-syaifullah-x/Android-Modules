@file:JvmName("FragmentInvokeListenerKtx")

package id.xxx.module.fragment.ktx

import android.util.Log
import androidx.fragment.app.Fragment

inline fun <reified ListenerType> Fragment.invokeListener(
    listenerClass: Class<ListenerType>,
    methodeName: String,
    vararg args: Any
) = ((parentFragment ?: activity) as? ListenerType)?.also { listener ->
    listenerClass.methods.forEach { method ->
        if (method.name == methodeName)
            method.invoke(listener, *args); return@also
    }
    throw NoSuchMethodException(methodeName)
} ?: Log.w(
    this::class.java.simpleName,
    "no listener, want to listen from ${this::class.java.simpleName}, " +
        "please implement ${ListenerType::class.java.simpleName} " +
        "in ${activity?.javaClass?.simpleName ?: "Activity"} | ${parentFragment?.javaClass?.simpleName ?: "Parent Fragment"}"
)