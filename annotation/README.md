# Module Annotation

```bash
gradlew :annotation:publishToMavenLocal
```

```gradle
repositories {
    mavenLocal()
}

dependencies {
    implementation "id.xxx.module:annotation:$vModule"
}
```

---