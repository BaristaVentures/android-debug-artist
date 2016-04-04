package com.barista_v.debugging;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.barista_v.debugging.item.input.InputItemListener;
import com.barista_v.debugging.item.phoenix.RestartListener;
import com.barista_v.debugging.item.spinner.SpinnerDrawerItem;
import com.barista_v.debugging.item.spinner.SpinnerItemListener;
import com.facebook.stetho.Stetho;
import com.github.pedrovgs.lynx.LynxActivity;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Debug drawer showing some debugging tools and info.
 *
 * To add dynamic actions check "debugDrawer.with*" methods.
 */
public class DebugDrawer implements OnCheckedChangeListener, Drawer.OnDrawerItemClickListener,
    SpinnerItemListener {
  WeakReference<AppCompatActivity> mActivityViewWeakReference;
  WeakReference<Application> mApplicationWeakReference;
  private Drawer mMenuDrawer;
  private WeakReference<ScalpelFrameLayout> mWeakScalpelLayout;
  @Nullable private RestartListener mRestartListener;
  @Nullable private InputItemListener mInputItemListener;

  public DebugDrawer(Application application, AppCompatActivity activity) {
    mActivityViewWeakReference = new WeakReference<>(activity);
    mApplicationWeakReference = new WeakReference<>(application);

    mMenuDrawer = new DrawerBuilder(activity)
        .withTranslucentStatusBar(true)
        .withDrawerGravity(Gravity.END)
        .build();

    addSwitchDrawerItem("Leak Canary", R.id.drawer_dev_item_leak);
    addSwitchDrawerItem("Stetho (Chrome debug bridge)", R.id.drawer_dev_item_stetho);
    addSwitchDrawerItem("Lynks (logs)", R.id.drawer_dev_item_lynks);
    addSwitchDrawerItem("Picasso Logs", R.id.drawer_dev_item_picasso);

    mMenuDrawer.setOnDrawerItemClickListener(this);
  }

  public void openDrawer() {
    mMenuDrawer.openDrawer();
  }

  public DebugDrawer withProperties(Map<String, String> properties) {
    for (Map.Entry<String, String> entry : properties.entrySet()) {
      mMenuDrawer.addItem(new SecondaryDrawerItem().withName(entry.getKey())
          .withIcon(R.drawable.ic_info_grey_700_24dp)
          .withDescription(entry.getValue())
          .withIdentifier(R.id.drawer_dev_item_info));
    }

    return this;
  }

  public DebugDrawer withSpinnerItem(int id, String name, String[] options, String selectedItem,
      SpinnerItemListener listener) {
    mMenuDrawer.addItem(new SpinnerDrawerItem(id, options, listener, selectedItem)
        .withMoreListeners(this)
        .withName(name));
    return this;
  }

  public DebugDrawer withSpinnerItem(int id, String name, String[] options, int selectedItem,
      SpinnerItemListener listener) {
    mMenuDrawer.addItem(new SpinnerDrawerItem(id, options, listener, selectedItem)
        .withMoreListeners(this)
        .withName(name));
    return this;
  }

  public DebugDrawer withDivider() {
    mMenuDrawer.addItem(new DividerDrawerItem());
    return this;
  }

  /**
   * Add an item that shows an input dialog.
   */
  public DebugDrawer withInputItem(int id, String name, InputItemListener inputItemListener) {
    mInputItemListener = inputItemListener;

    mMenuDrawer.addItem(
        new PrimaryDrawerItem().withName(String.format("Enter value for '%s'", name))
            .withTag(id)
            .withIcon(R.drawable.ic_textsms_grey_700_18dp)
            .withIdentifier(R.id.drawer_dev_item_input));
    return this;
  }

  public DebugDrawer withScalpelLayout(@Nullable ScalpelFrameLayout layout) {
    if (layout != null) {
      mWeakScalpelLayout = new WeakReference<>(layout);
      addSwitchDrawerItem("Scalpel", R.id.drawer_dev_item_scalpel);
    }
    return this;
  }

  public DebugDrawer withRestartListener(RestartListener restartListener) {
    mRestartListener = restartListener;

    mMenuDrawer.addItems(
        new PrimaryDrawerItem().withName("Restart App")
            .withIdentifier(R.id.drawer_dev_item_phoenix_app)
            .withIcon(R.drawable.ic_gavel_grey_700_18dp),

        new PrimaryDrawerItem().withName("Restart Activity")
            .withIdentifier(R.id.drawer_dev_item_phoenix_activity)
            .withIcon(R.drawable.ic_rowing_grey_700_18dp));

    return this;
  }

  //<editor-fold desc="OnCheckedChangeListener">
  @Override
  public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView,
      boolean isChecked) {
    AppCompatActivity activity = mActivityViewWeakReference.get();
    Application application = mApplicationWeakReference.get();
    if (activity == null || application == null) {
      return;
    }

    long identifier = drawerItem.getIdentifier();

    if (identifier == R.id.drawer_dev_item_scalpel) {
      ToggleDrawerItem toggleDrawerItem = (ToggleDrawerItem) drawerItem;
      ScalpelFrameLayout scalpelFrameLayout = mWeakScalpelLayout.get();
      if (scalpelFrameLayout != null) {
        boolean enabled = toggleDrawerItem.isChecked();
        scalpelFrameLayout.setLayerInteractionEnabled(enabled);
        scalpelFrameLayout.setDrawViews(enabled);
        scalpelFrameLayout.setChromeShadowColor(android.R.color.black);
      }
    } else if (identifier == R.id.drawer_dev_item_stetho) {
      Context context = activity.getApplicationContext();
      if (!buttonView.isChecked()) {
        showDialog("Stetho cant be disabled.");
        buttonView.setChecked(true);
      }
      if (buttonView.isChecked()) {
        Stetho.initializeWithDefaults(context);
        showDialog("chrome://inspect");
      }
    } else if (identifier == R.id.drawer_dev_item_leak) {
      if (!buttonView.isChecked()) {
        showDialog("Leak Canary cant be disabled.");
        buttonView.setChecked(true);
      }

      LeakCanary.install(application);
    } else if (identifier == R.id.drawer_dev_item_lynks) {
      buttonView.setChecked(false);
      Intent lynxActivityIntent = LynxActivity.getIntent(activity.getApplicationContext());
      activity.startActivity(lynxActivityIntent);
    } else if (identifier == R.id.drawer_dev_item_picasso) {
      if (!buttonView.isChecked()) {
        buttonView.setChecked(true);
      }

      Picasso picasso = Picasso.with(activity);
      picasso.setIndicatorsEnabled(true);
      picasso.setLoggingEnabled(true);
      StatsSnapshot picassoStats = picasso.getSnapshot();

      showDialog("Log and Indicators enabled: " +
          "\ngreen (memory, best performance)\n" +
          " blue (disk, good performance)\n" +
          " red (network, worst performance)." +
          "\n\nStats(on logs too): \n" + picassoStats.toString());

      Log.i("DEBUG", "Picasso stats:" + picassoStats.toString());
    }
  }
  //</editor-fold>

  //<editor-fold desc="OnDrawerItemSelectedListener">
  @Override
  public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
    long identifier = drawerItem.getIdentifier();
    if (identifier == R.id.drawer_dev_item_info) {
      SecondaryDrawerItem secondaryDrawerItem = ((SecondaryDrawerItem) drawerItem);
      secondaryDrawerItem.withSetSelected(false);
      showDialog(secondaryDrawerItem.getDescription().getText());
    } else if (identifier == R.id.drawer_dev_item_phoenix_activity) {
      restartActivity();
    } else if (identifier == R.id.drawer_dev_item_phoenix_app) {
      restartApp();
    } else if (identifier == R.id.drawer_dev_item_input) {
      showInputDialog(((PrimaryDrawerItem) drawerItem));
    }

    return true;
  }
  //</editor-fold>

  private void showDialog(String dialog) {
    AppCompatActivity activity = mActivityViewWeakReference.get();
    if (activity == null) {
      return;
    }

    Toast.makeText(activity, dialog, Toast.LENGTH_LONG).show();
  }

  private void addSwitchDrawerItem(String text, int id) {
    SwitchDrawerItem item = new SwitchDrawerItem().withName(text).withIdentifier(id);
    item.withOnCheckedChangeListener(this);
    mMenuDrawer.addItem(item);
  }

  public void restartApp() {
    Context context = mApplicationWeakReference.get();
    if (context != null) {
      if (mRestartListener != null) {
        mRestartListener.onAppRestart();
      }
      ProcessPhoenix.triggerRebirth(context);
    }
  }

  public void restartActivity() {
    Context context = mActivityViewWeakReference.get();
    if (context != null) {
      if (mRestartListener != null) {
        mRestartListener.onActivityRestart();
      }
      ProcessPhoenix.triggerRebirth(context);
    }
  }

  private void showInputDialog(final PrimaryDrawerItem drawerItem) {
    final Activity activity = mActivityViewWeakReference.get();
    if (activity == null) {
      return;
    }

    LayoutInflater factory = LayoutInflater.from(activity);
    final EditText entryView = (EditText) factory.inflate(R.layout.input_view, null);

    new AlertDialog.Builder(activity).
        setTitle(drawerItem.getName().toString())
        .setView(entryView)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            if (mInputItemListener != null) {
              String inputText = entryView.getText().toString();
              mInputItemListener.onOkClick((int) drawerItem.getTag(), inputText);
              drawerItem.withDescription(inputText);
              mMenuDrawer.updateItem(drawerItem);
              Toast.makeText(activity, "Selected: " + inputText, Toast.LENGTH_LONG).show();
            }
          }
        }).show();
  }

  @Override
  public void onSpinnerItemClick(SpinnerDrawerItem item, int itemId, CharSequence title) {
    item.withDescription(title.toString());
    mMenuDrawer.updateItem(item);
  }
}

