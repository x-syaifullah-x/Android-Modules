package id.xxx.module.ktx.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import id.xxx.module.common_android.databinding.ContainerMainBinding
import org.junit.Rule

class ActivityViewBindingKtxTest {

    companion object {
        const val DATA = "EXAMPLE DATA"
    }

    class ExampleActivity : AppCompatActivity() {

        private val viewBinding by viewBinding { layoutInflater ->
            ContainerMainBinding.inflate(layoutInflater)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(viewBinding.root)

            viewBinding.tvHead.text = DATA
        }
    }

    @get:Rule
    var activityScenarioRule =
        ActivityScenarioRule(ExampleActivity::class.java)

    @org.junit.Test
    fun test() {
        Espresso.onView(ViewMatchers.withText(DATA))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBackUnconditionally()
    }
}