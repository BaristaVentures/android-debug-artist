package debugartist.sample;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import debugartist.menu.ViewServer;
import debugartist.menu.drawer.DebugDrawer;
import debugartist.menu.drawer.item.input.InputItemListener;
import debugartist.menu.drawer.item.spinner.SpinnerDrawerItem;
import debugartist.menu.drawer.item.spinner.SpinnerItemListener;

public class BaseActivity extends AppCompatActivity
    implements SpinnerItemListener, InputItemListener {

  private DebugDrawer debugDrawer;

  @Override
  protected void onResume() {
    super.onResume();

    // Define a list of strings that withSpinnerItem will need
    String[] hosts = new String[] { "Value 1", "Value 2" };

    // Create debug drawer with selected features
    debugDrawer = new DebugDrawer(this, true)
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
  public void onTextInputEnter(int itemId, String inputText) {
    Toast.makeText(this, inputText, Toast.LENGTH_LONG).show();
  }
  //</editor-fold>
}
