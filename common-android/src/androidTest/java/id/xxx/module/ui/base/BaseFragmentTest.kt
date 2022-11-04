package id.xxx.module.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import id.xxx.module.common_android.databinding.FragmentMainBinding
import org.junit.Test

class BaseFragmentTest {

    companion object {

        private const val MOCK_DATA = "FRAGMENT HEAD"
    }

    class ExampleFragment : BaseFragment() {

        private val binding by viewBinding(FragmentMainBinding::class)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding.tvHead.text = MOCK_DATA
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ) = binding.root
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