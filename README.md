# Android Module Template

![Build](https://shields.io/github/workflow/status/x-syaifullah-x/android-module/build__publish_maven_local/master?event=push&logo=github&label=Build)
[![x-syaifullah-x](https://circleci.com/gh/x-syaifullah-x/android-module/tree/master.svg?style=svg)](https://circleci.com/gh/x-syaifullah-x/android-module/tree/master)

### Publish all module to maven local
```bash
./gradlew publishToMavenLocal
```

### Publish selected module to maven local
```
./gradlew :module_name:publishToMavenLocal
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
