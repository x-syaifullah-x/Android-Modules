# Module Started Android Library

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

- ### <b>API_KEY</b> required for requests to the server
  #### you can look at id.xxx.module.auth.repository.source.remote.constant.Firebase
    ```kotlin
        internal object Firebase {

            private const val API_KEY = "your_api_key"
  
        }
    ```
---

### Example
```kotlin
class MainActivity : AppCompatActivity() {
    
    private val authActivityResultLauncher =
        registerForActivityResult(AuthActivityForResult()) { result ->
            val uid = result?.uid
            val token = result?.token
            val refreshToken = result?.refreshToken
            val expiresIn = Date(result?.expiresInTimeMillis ?: 0)
            val isNewUser = "${result?.isNewUser}"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        authActivityResultLauncher.launch(null)
    }
}
```