package com.barista_v.sample;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;
import com.barista_v.debugging.DebugDrawer;
import com.barista_v.debugging.ViewServer;
import com.barista_v.debugging.item.input.InputItemListener;
import com.barista_v.debugging.item.phoenix.RestartListener;
import com.barista_v.debugging.item.spinner.SpinnerDrawerItem;
import com.barista_v.debugging.item.spinner.SpinnerItemListener;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SpinnerItemListener,
    RestartListener, InputItemListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
    }

    String[] hosts = new String[] { "Value 1", "Value 2" };
    ViewServer.get(this).addWindow(this);
    new DebugDrawer(MyApplication.sInstance, this)
        .withScalpelLayout((ScalpelFrameLayout) findViewById(R.id.scalpel))
        .withDivider()
        .withRestartListener(this)
        .withInputItem(2, "Host", this)
        .withSpinnerItem(1, "Spinner with item selected by index", hosts, 0, this)
        .withSpinnerItem(3, "Spinner with 2nd item selected", hosts, "Value", this)
        .withDivider()
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
  public void onSpinnerItemClick(SpinnerDrawerItem item, int itemId, CharSequence title) {
    Toast.makeText(this, "Selected: " + title, Toast.LENGTH_LONG).show();
  }

  //<editor-fold desc="RestartListener">
  @Override
  public void onAppRestart() {
    Log.d("DEBUG", "BOOM App");
  }

  @Override
  public void onActivityRestart() {
    Log.d("DEBUG", "BOOM Activity");
  }
  //</editor-fold>

  //<editor-fold desc="InputItemListener">
  @Override
  public void onOkClick(int itemId, String inputText) {
    Toast.makeText(this, inputText, Toast.LENGTH_LONG).show();
  }
  //</editor-fold>
}
