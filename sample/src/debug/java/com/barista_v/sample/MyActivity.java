package com.barista_v.sample;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.barista_v.debug_artist.ViewServer;
import com.barista_v.debug_artist.drawer.DebugDrawer;
import com.barista_v.debug_artist.item.input.InputItemListener;
import com.barista_v.debug_artist.item.phoenix.RestartListener;
import com.barista_v.debug_artist.item.spinner.SpinnerDrawerItem;
import com.barista_v.debug_artist.item.spinner.SpinnerItemListener;
import com.barista_v.debug_artist.repositories.PivotalReportRepository;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyActivity extends AppCompatActivity
    implements SpinnerItemListener, RestartListener, InputItemListener {

  private DebugDrawer mDebugDrawer;

  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);

    String[] hosts = new String[] { "Value 1", "Value 2" };

    mDebugDrawer = new DebugDrawer(MyApplication.sInstance, this)
        .withScalpelSwitch((ScalpelFrameLayout) findViewById(R.id.scalpelLayout))
        .withLeakCanarySwitch(true)
        .withPicassoLogsSwitch(true)
        .withStethoSwitch(true)
        .withReportBugReportSwitch(true,
            new PivotalReportRepository("8d4f9d99c2a4c818746fe1cb9015e2c9", "1954523"))
        .withDivider()
        .withLynksButton()
        .withPhoenixRestartButton(this)
        .withDivider()
        .withInputItem(2, "Host", this)
        .withSpinnerItem(1, "Spinner with item selected by index", hosts, 0, this)
        .withDivider()
        .withInfoProperties(getProperties());

    ViewServer.get(this).addWindow(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    mDebugDrawer.release();

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
      put("Manufacturer", Build.MANUFACTURER);
      put("Model", Build.MODEL);
    }};
  }

  @Override
  public void onSpinnerItemClick(SpinnerDrawerItem item, int itemId, CharSequence title) {
    Toast.makeText(this, "Selected: " + title, Toast.LENGTH_LONG).show();
  }

  @Override
  public void onAppRestarted() {
    Log.d("DEBUG", "BOOM App");
  }

  @Override
  public void onTextInputEnter(int itemId, String inputText) {
    Toast.makeText(this, inputText, Toast.LENGTH_LONG).show();
  }
}
