package id.xxx.module.auth.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes private val layout: Int) : Fragment() {

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = LayoutInflater.from(inflater.context)
            .inflate(layout, container, false)
        if (view.background == null)
            view.background = activity?.window?.decorView?.background
        return view
    }
}