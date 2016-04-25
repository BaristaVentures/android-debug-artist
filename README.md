# Debug Jedi

![Image](img/android.gif)

## Features

* Debug Drawer: add a debug drawer with info about the app and some debugging tools.

![Image2](img/debug_drawer.gif)

- To use `Restart App` and `Restart Activity` you should add `android.intent.category.DEFAULT` to
the activity you want to launch after rebirth.

example:

```
<activity android:name=".MainActivity">
  <intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.LAUNCHER" />
  </intent-filter>
</activity>
```

* ViewServer: helps to debug layouts

## Build 

Include it in your `gradle.properties`:

* Release:

```
repositories {
  maven {
      url 'http://archiva.barista-v.com:8080/repository/internal/'
      credentials {
        username "${NEXUS_USERNAME}"
        password "${NEXUS_PASSWORD}"
      }
  }
}

dependencies {
  devCompile 'com.barista_v:debugging:<version>@aar'
}
```


## Publish

1. Check `gradle.properties` and `debugging/gradle.properties` for POM setup.
1. Run: `scripts/publish.sh`

## Using
https://raw.github.com/danielgomezrico/gradle-mvn-push/master/gradle-mvn-push.gradle
