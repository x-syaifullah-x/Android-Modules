package id.xxx.module.ui.base;

import androidx.fragment.app.FragmentActivity;

import id.xxx.module.ktx.fragment.FragmentActivityKtx;

public abstract class BaseFragmentActivity extends FragmentActivity {

    @Override
    public void onBackPressed() {
        if (FragmentActivityKtx.fragmentStackIsEmpty(this)) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}
