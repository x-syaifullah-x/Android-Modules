# Module Started Java Library

```bash
gradlew :modeule_name:publishToMavenLocal
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