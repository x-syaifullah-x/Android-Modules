package id.xxx.module.ktx.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import id.xxx.module.common_android.databinding.ContainerMainBinding
import id.xxx.module.fragment.ktx.viewBinding
import org.junit.Test

class FragmentViewBindingKtxTest {

    companion object {
        const val DATA = "MOCK DATA"
        const val DATA_CHANGE = "MOCK DATA CHANGE"
    }

    class ExampleFragment : androidx.fragment.app.Fragment() {

        private val viewBinding by viewBinding<ContainerMainBinding>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            viewBinding.tvHead.text = DATA
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ) = viewBinding.root

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewBinding.tvHead.setOnClickListener {
                viewBinding.tvHead.text = DATA_CHANGE
            }
        }
    }

    @Test
    fun test() {
        launchFragmentInContainer<ExampleFragment>()
        Espresso
            .onView(ViewMatchers.withText(DATA))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
        Espresso
            .onView(ViewMatchers.withText(DATA_CHANGE))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBackUnconditionally()
    }
}