# Module Model

```bash
gradlew :model:publishToMavenLocal
```

```gradle
repositories {
    mavenLocal()
}

dependencies {
    implementation "id.xxx.module:model:$vModule"
}
```
---