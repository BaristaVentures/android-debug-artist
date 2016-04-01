package com.barista_v.sample;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.barista_v.debugging.DebugDrawer;
import com.barista_v.debugging.ViewServer;
import com.barista_v.debugging.item.spinner.SpinnerItemListener;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SpinnerItemListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
    }

    String[] hosts = new String[] { "http://asdasdasdasdasdasdasdasd.com", "http://445444.com" };
    ViewServer.get(this).addWindow(this);
    new DebugDrawer(MyApplication.sInstance, this)
        .withScalpelLayout((ScalpelFrameLayout) findViewById(R.id.scalpel))
        .withSpinnerItem(1, "Host", hosts, this)
        .withProperties(getProperties())
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
  public void onItemClick(int itemId, CharSequence title) {
    Toast.makeText(this, "Selected: " + title, Toast.LENGTH_LONG).show();
  }
}
