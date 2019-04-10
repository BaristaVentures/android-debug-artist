# Debug Drawer

## How to use it

**Please** check the `sample/` project.

1. Add this on `BaseActivity.onResume` so your child activities always add it to each activity:

- _Note: You **can** create a `BaseActivity` class and make everything inherit from it, the idea is that
you just put this code in your `debug` buildType and not in `release`._

- _Note: you build your own menu with the features you want_

Java:
```java
  private DebugDrawer mDebugDrawer;

  @Override
  protected void onResume() {
    super.onResume();

    String[] hosts = new String[] { "Value 1", "Value 2" };

    // Create debug drawer with selected features
    mDebugDrawer = new DebugDrawer(applicationContext, activityContext)
        .withInputItem(2, "Host", this)
        .withSpinnerItem(1, "Spinner with item selected by index", hosts, 0, this)
        .withDivider()
        .withInfoProperties(dictionaryWithProperties);
  }
```

2. Release resources on onPause:

Java:

```java
  @Override
  protected void onPause() {
    super.onPause();

    mDebugDrawer.release();
  }
```

### Features:

- Custom spinners: used sometimes to select from a list of hosts.
- Custom text input fields: used to set the app api host dynamically.
- Custom map of properties: map of `title` -> `content` that allow you to show  useful info about the app like version, current host, flavor, etc...