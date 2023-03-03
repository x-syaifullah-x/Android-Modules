package id.xxx.module.activity.base;

import androidx.appcompat.app.AppCompatActivity;

import id.xxx.module.fragment.ktx.FragmentActivityKtx;

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
