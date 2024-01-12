# MODULE GOOGLE SIGN

### How to use

#### Added meta-data in AndroidManifest.xml

- serverClientId from (Authentication -> Sign-in method -> Sign-in providers -> Google -> Web SDK
  configuration -> Web client ID)
- and add (Project settings -> Your apps -> Add app -> Android)
- and add fingerprint SHA-1

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

  <application>
    <meta-data
            android:name="${applicationId}.google_sign.server_client_id"
            android:value="0123456789-ABCDEFGHIJKLMNOPQRSTUVWXYZ.apps.googleusercontent.com" />
  </application>
</manifest>
```

```kotlin
class MainActivity : AppCompatActivity() {
    private val activityResultLauncher =
        registerForActivityResult(GoogleAccountContract()) { result: GoogleSignInAccount? ->
            if (result != null) {
                println(result)
            } else {
                println("Canceled")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultLauncher.launch(null)
    }
}
```

---

```bash
gradlew :module_name:publishToMavenLocal
```

```gradle
repositories {
    mavenLocal()
}

dependencies {
    implementation "id.xxx.module:module_name:$vModule"
}
```

---