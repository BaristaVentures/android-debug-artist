package com.barista_v.sample;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

/**
 * Should only be used in debug builds (do not save application statically)
 */
public class MyApplication extends Application {
  public static MyApplication sInstance;

  @Override
  public void onCreate() {
    super.onCreate();

    // Dont initialize anything in this process since is recreated by LeakCanary
    if (LeakCanary.isInAnalyzerProcess(this)) return;

    //
    // "Setup firebase"
    //

    sInstance = this;
  }
}
