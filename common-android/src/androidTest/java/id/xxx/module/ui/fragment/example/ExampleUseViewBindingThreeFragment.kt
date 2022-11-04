package id.xxx.module.ui.fragment.example

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import id.xxx.module.common_android.R
import id.xxx.module.common_android.databinding.BodyBinding
import id.xxx.module.ui.base.BaseFragment
import org.junit.Test

class ExampleUseViewBindingThreeFragment : BaseFragment(R.layout.container) {

    companion object {
        private val TAG = ExampleUseViewBindingThreeFragment::class.java.simpleName
        private const val TEXT_HEAD = "HEAD"
    }

    private val bodyBinding by viewBinding(
        BodyBinding::class,
        viewGroup = { view?.findViewById(R.id.container) },
        onDestroy = { Log.i(TAG, "$it") }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bodyBinding.tvHello.text = TEXT_HEAD
    }

    @Test
    fun launchFragmentInContainer() {
        launchFragmentInContainer<ExampleUseViewBindingThreeFragment>()
        Espresso
            .onView(ViewMatchers.withText(TEXT_HEAD))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBackUnconditionally()
    }
}