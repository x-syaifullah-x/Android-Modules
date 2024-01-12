# Module View Binding

### Enable view binding gradle
---
```gradle
android {

    buildFeatures {
        viewBinding true
    }
}
```

[using view binding in activity please see here](https://github.com/x-syaifullah-x/test_test/blob/master/common-android/src/androidTest/java/id/xxx/module/ktx/activity/ActivityViewBindingKtxTest.kt)
--

[using view binding in fragment please see here](https://github.com/x-syaifullah-x/test_test/blob/master/common-android/src/androidTest/java/id/xxx/module/ktx/fragment/FragmentViewBindingKtxTest.kt)
--

Activity
-
```kotlin
class MainActivity : AppCompatActivity() {

    private val viewBinding by viewBinding<ActivityMainBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}
```

Fragment
-
```kotlin
// initialization in constructor
class MyFragment : Fragment(R.layout.fragment_your) {

    private val viewBinding by viewBinding<FragmentYourBinding>()
}

class MyFragment : Fragment(R.layout.fragment_your) {

    private val viewBinding by viewBinding(FragmentYourBinding::class)
}

class MyFragment : Fragment(R.layout.fragment_your) {

    private val viewBinding by ViewBinding(FragmentYourBinding::class)
}

// initialization in onCreateView
class MyFragment : Fragment() {

    private val viewBinding by viewBinding<FragmentYourBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = viewBinding.root
}

class MyFragment : Fragment() {

    private val viewBinding by viewBinding(FragmentYourBinding::class)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = viewBinding.root
}

class MyFragment : Fragment() {

    private val viewBinding by ViewBinding(FragmentYourBinding::class)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = viewBinding.root
}
```

---
Publish To Maven Local
-
```bash
./gradlew :viewbinding:publishToMavenLocal
```
---
Dependencies
-
```gradle
repositories {
    mavenLocal()
}

dependencies {
    implementation "id.xxx.module:viewbinding:$vModule"
}
```
