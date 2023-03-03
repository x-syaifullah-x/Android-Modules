package id.xxx.module.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.viewbinding.ViewBinding
import id.xxx.module.common_android.databinding.ContainerMainBinding
import id.xxx.module.fragment.base.BaseFragmentActivityViewBinding
import id.xxx.module.fragment.ktx.viewBinding
import org.junit.Rule
import org.junit.Test

class BaseFragmentActivityViewBindingTest {

    companion object {
        const val TEXT_PARENT = "TEXT PARENT"
        const val TEXT_CHILD = "TEXT CHILD"
    }

    class ExampleActivity : BaseFragmentActivityViewBinding<ContainerMainBinding>() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            viewBinding.tvHead.text = TEXT_PARENT

            supportFragmentManager.beginTransaction()
                .add(viewBinding.frameBody.id, Fragment())
                .commit()
        }
    }

    class Fragment : androidx.fragment.app.Fragment() {

        private val binding by viewBinding<CostumeViewBinding>()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ) = binding.root

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.view.text = TEXT_CHILD
        }
    }

    class CostumeViewBinding(
        val view: TextView
    ) : ViewBinding {
        companion object {
            @JvmStatic
            @Suppress("unused", "UNUSED_PARAMETER")
            fun inflate(
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup?,
                attachToParent: Boolean
            ) = CostumeViewBinding(TextView(layoutInflater.context))
        }

        override fun getRoot() = view
    }

    @get:Rule
    var activityScenarioRule =
        ActivityScenarioRule(ExampleActivity::class.java)

    @Test
    fun test() {
        Espresso.onView(ViewMatchers.withText(TEXT_PARENT))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(TEXT_CHILD))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBackUnconditionally()
    }
}