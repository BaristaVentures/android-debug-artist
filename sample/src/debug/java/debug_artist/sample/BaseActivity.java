package debug_artist.sample;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import debug_artist.menu.ViewServer;
import debug_artist.menu.drawer.DebugDrawer;
import debug_artist.menu.drawer.item.input.InputItemListener;
import debug_artist.menu.drawer.item.phoenix.RestartListener;
import debug_artist.menu.drawer.item.spinner.SpinnerDrawerItem;
import debug_artist.menu.drawer.item.spinner.SpinnerItemListener;
import debug_artist.menu.report_bug.BugRepository;
import debug_artist.reporter_pivotaltracker.PivotalBugRepositoryBuilder;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.os.Build.MANUFACTURER;
import static android.os.Build.MODEL;

public class BaseActivity extends AppCompatActivity
    implements SpinnerItemListener, RestartListener, InputItemListener {

  private DebugDrawer mDebugDrawer;

  @Override
  protected void onResume() {
    super.onResume();

    String[] hosts = new String[] { "Value 1", "Value 2" };

    BugRepository.Builder repositoryBuilder =
        new PivotalBugRepositoryBuilder(BuildConfig.PIVOTAL_API_KEY,
            BuildConfig.PIVOTAL_PROJECT_ID, getProperties(),
            new String[] { "android-sample" });

    // Create debug drawer with selected features
    mDebugDrawer = new DebugDrawer(BaseApplication.sInstance, this)
        .withScalpelSwitch((ScalpelFrameLayout) findViewById(R.id.scalpelLayout))
        .withLeakCanarySwitch(true)
        .withPicassoLogsSwitch(true)
        .withStethoSwitch(true)
        .withShakeToReportBugSwitch(false, repositoryBuilder)
        .withDivider()
        .withLynksButton()
        .withPhoenixRestartButton(this)
        .withDivider()
        .withInputItem(2, "Host", this)
        .withSpinnerItem(1, "Spinner with item selected by index", hosts, 0, this)
        .withDivider()
        .withInfoProperties(getProperties());

    // Enable view debuger on some devices
    ViewServer.get(this).addWindow(this);
  }

  @Override
  protected void onPause() {
    super.onPause();

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
      put("Manufacturer", MANUFACTURER);
      put("Model", MODEL);
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
