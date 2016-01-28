Debug helpers for apps
=========================

![Image](android.gif)

# Setup in project 

Include it in your `gradle.properties`:

```
repositories {
  maven { url 'http://archiva.barista-v.com:8080/repository/barista/' }
}

dependencies {
  devCompile 'com.barista_v:debugging:0.0.1-SNAPSHOT'
}
```

# Use

Check sample module to see how it can be used.

- Debug Drawer: add a debug drawer with info about the app and some debugging tools.
- ViewServer.

# Publish

1. Check `gradle.properties` and `debugging/gradle.properties` for POM setup.
1. Run: `scripts/publish.sh`

# Using
https://raw.github.com/danielgomezrico/gradle-mvn-push/master/gradle-mvn-push.gradle
