package com.barista_v.sample;

import android.app.Application;

public class MyApplication extends Application {
  public static MyApplication sInstance;

  @Override
  public void onCreate() {
    super.onCreate();
    sInstance = this;
  }
}
