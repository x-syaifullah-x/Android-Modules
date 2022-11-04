package id.xxx.module.ui.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import id.xxx.module.ktx.activity.viewBinding

abstract class BaseAppCompatActivityViewBinding<T : ViewBinding> : BaseAppCompatActivity() {

    protected val viewBinding by viewBinding<T>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(viewBinding.root)
    }
}