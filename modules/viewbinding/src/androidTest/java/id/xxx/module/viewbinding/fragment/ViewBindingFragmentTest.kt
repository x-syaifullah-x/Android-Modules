package id.xxx.module.viewbinding.fragment

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import id.xxx.modules.viewbinding.databinding.FragmentMainBinding
import org.junit.Test

class ViewBindingFragmentTest {

    companion object {

        private const val MOCK_DATA = "FRAGMENT HEAD"
    }

    class ExampleFragment : ViewBindingFragment<FragmentMainBinding>() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            viewBinding.tvHead.text = MOCK_DATA
        }
    }

    @Test
    fun test() {
        launchFragmentInContainer<ExampleFragment>()
        Espresso
            .onView(ViewMatchers.withText(MOCK_DATA))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBackUnconditionally()
    }
}