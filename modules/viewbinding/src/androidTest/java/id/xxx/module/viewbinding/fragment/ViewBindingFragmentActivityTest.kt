package id.xxx.module.viewbinding.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import id.xxx.module.viewbinding.ktx.ActivityViewBindingKtxTest
import id.xxx.module.viewbinding.ktx.viewBinding
import id.xxx.modules.viewbinding.databinding.ContainerMainBinding
import org.junit.Rule

class ViewBindingFragmentActivityTest {

    class MainActivity : FragmentActivity() {

        private val viewBinding by viewBinding<ContainerMainBinding>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(viewBinding.root)

            viewBinding.tvHead.text = ActivityViewBindingKtxTest.DATA
        }
    }

    companion object {
        const val DATA = "EXAMPLE DATA"
    }

    @get:Rule
    val activityScenarioRule =
        ActivityScenarioRule(MainActivity::class.java)

    @org.junit.Test
    fun viewBindingTest() {
        Espresso.onView(ViewMatchers.withText(DATA))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBackUnconditionally()
    }
}