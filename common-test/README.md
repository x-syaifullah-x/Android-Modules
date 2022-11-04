# Module Started Java Library

```bash
gradlew :common-test:publishToMavenLocal
```

```gradle
repositories {
    mavenLocal()
}

dependencies {
    testImplementation "id.xxx.module:common-test:$vModule"
    
    androidTestImplementation "id.xxx.module:common-test:$vModule"
}
```

---