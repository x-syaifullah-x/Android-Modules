package id.xxx.module.auth.activity.impl

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity

class OnBackPressedCallbackImpl(
    private val activity: FragmentActivity
) : OnBackPressedCallback(true) {

    override fun handleOnBackPressed() {
        val fragments = activity.supportFragmentManager.fragments
        val isTopFragment = (fragments.size == 1)
        if (isTopFragment) {
            activity.finishAfterTransition()
        } else {
            activity.supportFragmentManager.beginTransaction()
                .remove(fragments.last())
                .commit()
        }
    }
}