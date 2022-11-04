package id.xxx.module.ui.base;

import androidx.appcompat.app.AppCompatActivity;

import id.xxx.module.ktx.fragment.FragmentActivityKtx;

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        if (FragmentActivityKtx.fragmentStackIsEmpty(this)) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}
