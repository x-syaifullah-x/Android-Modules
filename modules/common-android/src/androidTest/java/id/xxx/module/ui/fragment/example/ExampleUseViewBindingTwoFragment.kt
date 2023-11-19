package id.xxx.module.ui.fragment.example

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import id.xxx.module.common_android.databinding.BodyBinding
import id.xxx.module.common_android.databinding.HeadBinding
import id.xxx.module.fragment.base.BaseFragment
import org.junit.Test

class ExampleUseViewBindingTwoFragment : BaseFragment() {

    companion object {
        private val TAG = ExampleUseViewBindingThreeFragment::class.java.simpleName
        private const val TEXT_HEAD = "HEAD"
        private const val TEXT_BODY = "BODY"
    }

    private val bindingOne by viewBinding(HeadBinding::class)

    private val bindingTwo by viewBinding(
        BodyBinding::class,
        viewGroup = { bindingOne.frameLayout },
        onDestroy = { Log.i(TAG, "$it") }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingOne.tvHead.text = TEXT_HEAD
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = bindingOne.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingTwo.tvHello.text = TEXT_BODY
    }

    @Test
    fun launchFragmentInContainer() {
        launchFragmentInContainer<ExampleUseViewBindingTwoFragment>()
        Espresso
            .onView(ViewMatchers.withText(TEXT_HEAD))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso
            .onView(ViewMatchers.withText(TEXT_BODY))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.pressBackUnconditionally()
    }
}