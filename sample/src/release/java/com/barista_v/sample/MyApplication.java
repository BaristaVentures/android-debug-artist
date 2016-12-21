package com.barista_v.sample;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

/**
 * Should only be used in debug builds (do not save application statically)
 */
public class MyApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    //
    // "Setup firebase"
    //
  }
}
