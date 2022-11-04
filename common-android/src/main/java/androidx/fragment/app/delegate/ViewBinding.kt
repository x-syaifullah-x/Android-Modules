package androidx.fragment.app.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewBinding<ViewBindingClass : ViewBinding>(
    private val viewBindingClass: Class<ViewBindingClass>,
    private val viewGroup: () -> ViewGroup? = { null },
    private val onDestroy: (ViewBindingClass) -> Unit = { },
) : ReadOnlyProperty<Fragment, ViewBindingClass> {

    companion object {

        @JvmStatic
        internal fun <T : ViewBinding> Class<T>.getMethodeInflate() =
            try {
                getMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java
                )
            } catch (err: Throwable) {
                null
            }
    }

    private var _bindingClass: ViewBindingClass? = null

    private val lifecycleEventObserver = object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                onDestroy(_bindingClass ?: return)
                _bindingClass = null
            }
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>) =
        _bindingClass ?: synchronized(this) {
            _bindingClass ?: run {
                thisRef.viewLifecycleOwnerLiveData.observe(thisRef) { lifecycleOwner ->
                    lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)
                }

                val viewGroup = viewGroup.invoke()
                val attachToParent = (viewGroup != null)
                val inflater = thisRef.layoutInflater
                return inflate(inflater, viewGroup, attachToParent)
                    .also { _bindingClass = it }
            }
        }

    @Suppress("UNCHECKED_CAST")
    private fun inflate(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        attachToParent: Boolean
    ) = viewBindingClass
        .getMethodeInflate()
        ?.invoke(
            null,
            layoutInflater,
            viewGroup,
            attachToParent
        ) as ViewBindingClass

//    @Suppress("UNCHECKED_CAST")
//    private fun bind(view: View?) =
//        try {
//            viewBindingClass.getMethod(
//                "bind", View::class.java
//            ).invoke(null, view)
//        } catch (e: Throwable) {
//            ViewBinding { view ?: throw NullPointerException() }
//        } as ViewBindingClass

//    private fun getFragmentContainer(fragment: Fragment): ViewGroup? {
//        val mContainer = Fragment::class.java.getDeclaredField("mContainer")
//        mContainer.isAccessible = true
//        return mContainer.get(fragment) as? ViewGroup
//    }

//    private fun getContentLayoutId(obj: Fragment): Int {
//        val mContentLayoutId = Fragment::class.java.getDeclaredField("mContentLayoutId")
//        mContentLayoutId.isAccessible = true
//        return mContentLayoutId.getInt(obj)
//    }
}