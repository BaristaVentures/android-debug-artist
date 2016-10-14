package com.barista_v.sample;

import android.app.Application;

/**
 * Should only be used in debug builds (do not save application statically)
 */
public class MyApplication extends Application {
  public static MyApplication sInstance;

  @Override
  public void onCreate() {
    super.onCreate();
    sInstance = this;
  }
}
