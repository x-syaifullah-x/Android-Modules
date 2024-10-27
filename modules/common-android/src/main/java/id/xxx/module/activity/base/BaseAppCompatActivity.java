package id.xxx.module.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Collections;
import java.util.List;

import id.xxx.module.fragment.ktx.FragmentActivityKtx;

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            for (Fragment fragment : getChildFragment()) {
//                fragment.onActivityResult(requestCode, resultCode, data);
//            }
//        }
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        for (Fragment fragment : getChildFragment()) {
//            fragment.onOptionsItemSelected(item);
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    private List<Fragment> getChildFragment() {
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        if (!fragments.isEmpty()) {
//            Fragment fragment = fragments.get(0);
//            return fragment.getChildFragmentManager().getFragments();
//        }
//        return Collections.emptyList();
//    }

    /**
     * ┬───
     * │ GC Root: Global variable in native code
     * │
     * ├─ android.app.Activity$1 instance
     * │    Leaking: UNKNOWN
     * │    Retaining 2.1 MB in 36660 objects
     * │    Library leak match: instance field android.app.Activity$1#this$0
     * │    Anonymous subclass of android.app.IRequestFinishCallback$Stub
     * │    this$0 instance of id.movie.catalogue.presentation.ui.home.MainActivity with mDestroyed = true
     * │    ↓ Activity$1.this$0
     * │                 ~~~~~~
     * ╰→ id.movie.catalogue.presentation.ui.home.MainActivity instance
     * ​     Leaking: YES (ObjectWatcher was watching this because id.movie.catalogue.presentation.ui.home.MainActivity
     * ​     received Activity#onDestroy() callback and Activity#mDestroyed is true)
     * ​     Retaining 2.1 MB in 36659 objects
     * ​     key = 4fd45154-7480-470e-83cd-cee30b0ada8c
     * ​     watchDurationMillis = 5309
     * ​     retainedDurationMillis = 281
     * ​     mApplication instance of id.movie.catalogue.App
     * ​     mBase instance of androidx.appcompat.view.ContextThemeWrapper
     * Handle Leaking
     */
//    @Override
//    public void onBackPressed() {
//        if (FragmentActivityKtx.fragmentStackIsEmpty(this)) {
//            finishAfterTransition();
//        } else {
//            super.onBackPressed();
//        }
//    }
}
