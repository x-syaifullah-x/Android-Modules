package id.xxx.module.viewbinding.ktx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.delegate.ViewBinding
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import id.xxx.modules.viewbinding.R
import id.xxx.modules.viewbinding.databinding.ContainerMainBinding
import org.junit.Test

class FragmentViewBindingKtxTest {

    companion object {
        const val DATA = "MOCK DATA"
        const val DATA_CHANGE = "MOCK DATA CHANGE"
    }

    @Test
    fun test_one() {
        launchFragmentInContainer<ExampleOneFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.tv_head))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check { view, _ -> view.callOnClick() }
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(DATA_CHANGE)))
        Espresso.pressBackUnconditionally()
    }

    @Test
    fun test_two() {
        launchFragmentInContainer<ExampleTwoFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.tv_head))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check { view, _ -> view.callOnClick() }
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(DATA_CHANGE)))
        Espresso.pressBackUnconditionally()
    }

    @Test
    fun test_three() {
        launchFragmentInContainer<ExampleThreeFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.tv_head))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check { view, _ -> view.callOnClick() }
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(DATA_CHANGE)))
        Espresso.pressBackUnconditionally()
    }

    abstract class BaseFragment : Fragment() {

        protected abstract val viewBinding: ContainerMainBinding

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

    class ExampleOneFragment : BaseFragment() {
        override val viewBinding by ViewBinding(ContainerMainBinding::class)
    }

    class ExampleTwoFragment : BaseFragment() {

        override val viewBinding by viewBinding(ContainerMainBinding::class)
    }

    class ExampleThreeFragment : BaseFragment() {

        override val viewBinding by viewBinding<ContainerMainBinding>()
    }
}