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