package id.xxx.module.viewbinding.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.viewbinding.ViewBinding
import id.xxx.module.viewbinding.ktx.viewBinding
import id.xxx.modules.viewbinding.databinding.ContainerMainBinding
import id.xxx.modules.viewbinding.databinding.ContainerMainChildBinding
import org.junit.Test

class ViewBindingFragmentTestTwo {

    companion object {
        const val TEXT_HEAD = "TEXT HEAD"
        const val TEXT_BODY = "TEXT BODY"
    }

    abstract class FragmentA<A, B : ViewBinding, C> : ViewBindingFragment<B>()

    abstract class FragmentB<A, B : ViewBinding, C, D> : FragmentA<String, B, D>()

    class FragmentC : FragmentB<String, ContainerMainBinding, Int, Boolean>() {

        private val bodyBinding by viewBinding(
            ContainerMainChildBinding::class,
            viewGroup = { viewBinding.frameBody },
            attachToParent = true,
            onDestroy = {}
        )

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewBinding.tvHead.text = TEXT_HEAD
            bodyBinding.tvContent.text = TEXT_BODY
        }
    }

    @Test
    fun test() {
        launchFragmentInContainer<FragmentC>()
        Espresso
            .onView(ViewMatchers.withText(TEXT_HEAD))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso
            .onView(ViewMatchers.withText(TEXT_BODY))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBackUnconditionally()
    }
}