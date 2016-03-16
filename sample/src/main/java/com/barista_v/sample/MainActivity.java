package com.barista_v.sample;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.barista_v.debugging.DebugDrawer;
import com.barista_v.debugging.ScalpeledActivity;
import com.barista_v.debugging.ViewServer;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ScalpeledActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
    }

    ViewServer.get(this).addWindow(this);
    new DebugDrawer(MyApplication.sInstance, this)
        .addProperties(getProperties())
        .openDrawer();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ViewServer.get(this).removeWindow(this);
  }

  /**
   * Return a map with keys and values referencing environment variables
   */
  public Map<String, String> getProperties() {
    return new LinkedHashMap<String, String>() {{
      put("BuildType", BuildConfig.BUILD_TYPE);
      put("AppVersion", BuildConfig.VERSION_NAME);
      put("BuildNumber", String.valueOf(BuildConfig.VERSION_CODE));
      put("AndroidVersion", Build.VERSION.RELEASE);
      //put("GitSHA", BuildConfig.GIT_SHA);
      put("Manufacturer", Build.MANUFACTURER);
      put("Model", Build.MODEL);
    }};
  }

  @Override
  public ScalpelFrameLayout getScalpelFrameLayout() {
    return (ScalpelFrameLayout) findViewById(R.id.scalpel);
  }
}
