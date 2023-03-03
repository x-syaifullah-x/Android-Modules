package id.xxx.module.ui.fragment.example

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import id.xxx.module.common_android.databinding.HeadBinding
import id.xxx.module.fragment.base.BaseFragment
import org.junit.Test

class ExampleUseViewBindingOneFragment : BaseFragment() {

    companion object {
        private val TAG = ExampleUseViewBindingOneFragment::class.java.simpleName
        private const val TEXT_HEAD = "HEAD"
    }

    private val binding by viewBinding(HeadBinding::class) {
        Log.i(TAG, "$it")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvHead.text = TEXT_HEAD
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    @Test
    fun launchFragmentInContainer() {
        launchFragmentInContainer<ExampleUseViewBindingOneFragment>()
        Espresso
            .onView(ViewMatchers.withText(TEXT_HEAD))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBackUnconditionally()
    }
}