package id.xxx.templates.activities

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import id.xxx.template.activities.MainActivity
import org.junit.Rule

class MainActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @org.junit.Test
    fun mainActivityLaunchTest() {
        Espresso.onView(ViewMatchers.withId(android.R.id.content))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}