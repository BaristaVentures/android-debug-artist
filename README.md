![](img/logo.png)

This library make developers life easier with some tools, you can add it to your debug builds and have some debug
libraries like [Leakcanary](https://github.com/square/leakcanary) and others without extra work.

- Debug Drawer: add a right menu that enable some features like LeakCanary, stetho and others. *use just for debug builds*
- [ViewServer](https://github.com/romainguy/ViewServer): helps to debug layouts with more devices.

![](img/debug_drawer.jpg)

## How to use it

Add this on each activity `setContentView` so your child activities always add it to each activity:

```
new DebugDrawer(MyApplication.sInstance, this)
        .withScalpelSwitch((ScalpelFrameLayout) findViewById(R.id.scalpel))
        .withLeakCanarySwitch(true)
        .withPicassoLogsSwitch()
        .withStethoSwitch()
        .withDivider()
        .withLynksButton()
        .withPhoenixRestartButtons(this)
        .withInputItem(2, "Host", this)
        .withSpinnerItem(1, "Spinner with item selected by index", hosts, 0, this)
        .withSpinnerItem(3, "Spinner with 2nd item selected", hosts, "Value", this)
        .withDivider()
        .withInfoProperties(getProperties())
```

### Features:

For each feature you need to setup it correctly, sometimes no extra work is needed.
Features:
- [Links](https://github.com/pedrovgs/Lynx): show logcat live phone.
- [Leakcanary](https://github.com/square/leakcanary): track Memory leaks.

You need to avoid other thing initialization on `Application` `onCreate` since LeakCanary creates another process and
may cause crashes:

```
class MyCustomApp {

    @Override
    void onCreate(){
      if (LeakCanary.isInAnalyzerProcess(this)) return // <-------------- This

      // Setup Firebase
      // Setup Other Services
    }

}
```

- [Picasso logs](https://github.com/square/picasso): enable debug logs.
- [Scalpel](https://github.com/JakeWharton/scalpel): see 3d layouts.
- [Stetho](https://github.com/facebook/stetho): if you want custom interceptors you can add them and it will use them _automatically_.
- [Process Phoenix](https://github.com/JakeWharton/ProcessPhoenix): restart app/activity.
- Custom spinners: used sometimes to select from a list of hosts.
- Custom text input fields: used to set the app api host dynamically.
- Custom map of properties: map of `title` -> `content` that allow you to show  useful info about the app like version, current host, flavor, etc...

## Add to gradle project

```groovy
dependencies {
  compile "com.barista-v:debug-artist:0.5.9"
}
```

## Publish

You need to create `lib/bintray.properties` with the variables declared on `lib/bintray.properties.example`.

```
cp lib/gradle.properties.example lib/gradle.properties
```

## Thanks to
- All feature developers!
- [Barista Ventures](http://barista-v.com/)
- [AndroidTool-mac](https://github.com/mortenjust/androidtool-mac): helped to create the gif from device.
- [ImageOptim](https://github.com/ImageOptim/ImageOptim): optimize gif generated from ^^ :P
