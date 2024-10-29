package id.xxx.module.viewbinding.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import id.xxx.module.viewbinding.ktx.viewBinding
import id.xxx.modules.viewbinding.databinding.BodyBinding
import id.xxx.modules.viewbinding.databinding.HeadBinding
import org.junit.Test

class ViewBindingFragmentTestThree : ViewBindingFragment<HeadBinding>() {

    companion object {
        private val TAG = ViewBindingFragmentTestThree::class.java.simpleName
        private const val TEXT_HEAD = "HEAD"
        private const val TEXT_BODY = "BODY"
    }

    private val bindingTwo by viewBinding(
        BodyBinding::class,
        viewGroup = { viewBinding.frameLayout },
        attachToParent = true,
        onDestroy = { Log.i(TAG, "$it") }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.tvHead.text = TEXT_HEAD
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingTwo.tvHello.text = TEXT_BODY
    }

    @Test
    fun launchFragmentInContainer() {
        launchFragmentInContainer<ViewBindingFragmentTestThree>()
        Espresso
            .onView(ViewMatchers.withText(TEXT_HEAD))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso
            .onView(ViewMatchers.withText(TEXT_BODY))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.pressBackUnconditionally()
    }
}