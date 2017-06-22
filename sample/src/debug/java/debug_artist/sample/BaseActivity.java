package debug_artist.sample;

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
import debug_artist.menu.utils.device.AndroidDevice;
import debug_artist.reporter_pivotaltracker.PivotalBugRepositoryBuilder;
import java.util.Map;

public class BaseActivity extends AppCompatActivity
    implements SpinnerItemListener, RestartListener, InputItemListener {

  private DebugDrawer debugDrawer;

  @Override
  protected void onResume() {
    super.onResume();

    // Define a list of strings that withSpinnerItem will need
    String[] hosts = new String[] { "Value 1", "Value 2" };

    // Define your bug repository builder to happy report bugs to your third party services
    Map<String, String> properties = AndroidDevice.getProjectProperties(this);
    BugRepository.Builder repositoryBuilder =
        new PivotalBugRepositoryBuilder(BuildConfig.PIVOTAL_API_KEY,
            BuildConfig.PIVOTAL_PROJECT_ID, properties, new String[] { "android-sample" });

    BaseApplication applicationInstance = BaseApplication.sInstance;

    // Create debug drawer with selected features
    debugDrawer = new DebugDrawer(applicationInstance, this)
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
        .withInfoProperties();

    // Enable view debugger on some devices
    ViewServer.get(this).addWindow(this);
  }

  @Override
  protected void onPause() {
    super.onPause();

    debugDrawer.release();
    ViewServer.get(this).removeWindow(this);
  }

  //<editor-fold desc="DebugDrawer events for SpinnerItemListener, RestartListener, InputItemListener">
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
  //</editor-fold>
}
