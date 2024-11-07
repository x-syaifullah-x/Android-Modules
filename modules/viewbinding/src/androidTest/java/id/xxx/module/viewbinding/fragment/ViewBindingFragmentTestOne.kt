package id.xxx.module.viewbinding.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import id.xxx.module.viewbinding.ktx.FragmentViewBindingKtxTest.Companion.DATA_CHANGE
import id.xxx.modules.viewbinding.R
import id.xxx.modules.viewbinding.databinding.ContainerMainBinding
import org.junit.Test

class ViewBindingFragmentTestOne {

    companion object {

        const val TEXT_HEAD = "TEXT HEAD"
        const val TEXT_HEAD_CHANGE = "TEXT HEAD CHANGE"
    }

    @Test
    fun test() {
        launchFragmentInContainer<ExampleFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.tv_head))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check { view, _ -> view.callOnClick() }
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(TEXT_HEAD_CHANGE)))
        Espresso.pressBackUnconditionally()
    }

    class ExampleFragment : ViewBindingFragment<ContainerMainBinding>() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            viewBinding.tvHead.text = TEXT_HEAD
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            viewBinding.tvHead.setOnClickListener {
                viewBinding.tvHead.text = TEXT_HEAD_CHANGE
            }
        }
    }
}