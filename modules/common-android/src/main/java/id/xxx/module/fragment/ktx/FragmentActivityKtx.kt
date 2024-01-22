@file:JvmName("FragmentActivityKtx")

package id.xxx.module.fragment.ktx

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/*
    D/LeakCanary: 1 LIBRARY LEAKS
    D/LeakCanary:
    D/LeakCanary: A Library Leak is a leak caused by a known bug in 3rd party code that you do not have control over.
    D/LeakCanary: See https://square.github.io/leakcanary/fundamentals-how-leakcanary-works/#4-categorizing-leaks
    D/LeakCanary:
    D/LeakCanary: Leak pattern: instance field android.app.Activity$1#this$0
    D/LeakCanary: Description: Android Q added a new android.app.IRequestFinishCallback$Stub class. android.app.Activity creates an
    D/LeakCanary: implementation of that interface as an anonymous subclass. That anonymous subclass has a reference to the activity.
    D/LeakCanary: Another process is keeping the android.app.IRequestFinishCallback$Stub reference alive long after Activity.
    D/LeakCanary: onDestroyed() has been called, causing the activity to leak. Fix: You can "fix" this leak by overriding Activity.
    D/LeakCanary: onBackPressed() and calling Activity.finishAfterTransition(); instead of super if the activity is task root and the
    D/LeakCanary: fragment stack is empty. Tracked here: https://issuetracker.google.com/issues/139738913
    D/LeakCanary: 84365 bytes retained by leaking objects
    D/LeakCanary: Signature: 7573873eb76c9b7e8963efe04194842e7e15c8b9
    D/LeakCanary: ┬───
    D/LeakCanary: │ GC Root: Global variable in native code
    D/LeakCanary: │
    D/LeakCanary: ├─ android.app.Activity$1 instance
    D/LeakCanary: │    Leaking: UNKNOWN
    D/LeakCanary: │    Retaining 84.9 kB in 1860 objects
    D/LeakCanary: │    Library leak match: instance field android.app.Activity$1#this$0
    D/LeakCanary: │    Anonymous subclass of android.app.IRequestFinishCallback$Stub
    D/LeakCanary: │    this$0 instance of id.xxx.started_project.layer.presentation.home.MainActivity with mDestroyed = true
    D/LeakCanary: │    ↓ Activity$1.this$0
    D/LeakCanary: │                 ~~~~~~
    D/LeakCanary: ╰→ id.xxx.started_project.layer.presentation.home.MainActivity instance
    D/LeakCanary: ​     Leaking: YES (ObjectWatcher was watching this because id.xxx.started_project.layer.presentation.home.MainActivity
    D/LeakCanary: ​     received Activity#onDestroy() callback and Activity#mDestroyed is true)
    D/LeakCanary: ​     Retaining 84.4 kB in 1859 objects
    D/LeakCanary: ​     key = 9f98f6cb-5537-4b49-a597-04150a8d207d
    D/LeakCanary: ​     watchDurationMillis = 6195
    D/LeakCanary: ​     retainedDurationMillis = 1186
    D/LeakCanary: ​     mApplication instance of id.xxx.started_project.AppDebug
    D/LeakCanary: ​     mBase instance of androidx.appcompat.view.ContextThemeWrapper
    D/LeakCanary:
    D/LeakCanary: ====================================
*/
//@JvmName("fragmentStackIsEmpty")
//fun <T : FragmentActivity> T.fragmentStackIsEmpty(): Boolean {
//    val fragmentRoot = supportFragmentManager.fragments.firstOrNull()
//    val countParentFragment = fragmentRoot?.parentFragmentManager?.backStackEntryCount
//    val countChildFragment = fragmentRoot?.childFragmentManager?.backStackEntryCount
//    return (countParentFragment == 0 && countChildFragment == 0)
//}

inline fun <reified T : Fragment> FragmentActivity.getFragment(): T? {
    return supportFragmentManager.fragments.find { it is T } as? T
}
