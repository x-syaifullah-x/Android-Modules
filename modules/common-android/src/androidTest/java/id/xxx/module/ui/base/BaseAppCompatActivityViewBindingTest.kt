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
import id.xxx.module.activity.base.BaseAppCompatActivity
import id.xxx.module.viewbinding.fragment.ViewBindingFragment
import id.xxx.module.viewbinding.ktx.viewBinding
import id.xxx.modules.common_android.databinding.XMainBinding
import org.junit.Rule
import org.junit.Test

class BaseAppCompatActivityViewBindingTest {

    companion object {
        private const val DATA_PARENT = "DATA PARENT"
        private const val DATA_CHILD = "DATA CHILD"
    }

    class ExampleActivity : BaseAppCompatActivity() {

        private val viewBinding by viewBinding<XMainBinding>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(viewBinding.root)

            viewBinding.tvHead.text = DATA_PARENT

            supportFragmentManager.beginTransaction()
                .add(viewBinding.frameBody.id, Fragment())
                .commit()
        }
    }

    class Fragment : ViewBindingFragment<CostumeViewBinding>() {

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewBinding.view.text = DATA_CHILD
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
    val activityScenarioRule = ActivityScenarioRule(ExampleActivity::class.java)

    @Test
    fun test() {
        Espresso.onView(ViewMatchers.withText(DATA_PARENT))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(DATA_CHILD))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBackUnconditionally()
    }
}