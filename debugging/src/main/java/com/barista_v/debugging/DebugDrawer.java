package com.barista_v.debugging;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.facebook.stetho.Stetho;
import com.github.pedrovgs.lynx.LynxActivity;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Debug drawer showing some debugging tools and info.
 */
public class DebugDrawer implements OnCheckedChangeListener {
  WeakReference<AppCompatActivity> mActivityViewWeakReference;
  WeakReference<Application> mApplicationWeakReference;
  private Drawer mMenuDrawer;

  public DebugDrawer(Application application, AppCompatActivity activity) {
    mActivityViewWeakReference = new WeakReference<>(activity);
    mApplicationWeakReference = new WeakReference<>(application);

    mMenuDrawer = new DrawerBuilder(activity)
        .withTranslucentStatusBar(true)
        .withDrawerGravity(Gravity.END)
        .build();

    if (activity instanceof ScalpeledActivity) {
      ScalpeledActivity scalpeledActivity = (ScalpeledActivity) activity;
      if (scalpeledActivity.getScalpelFrameLayout() != null) {
        addToggleDrawerItem("Scalpel", R.id.drawer_dev_item_scalpel);
      }
    }

    addToggleDrawerItem("Leak Canary", R.id.drawer_dev_item_leak);
    addToggleDrawerItem("Stetho (Chrome debug bridge)", R.id.drawer_dev_item_stetho);
    addToggleDrawerItem("Lynks (logs)", R.id.drawer_dev_item_lynks);
    addToggleDrawerItem("Picasso Logs", R.id.drawer_dev_item_picasso);
  }

  public DebugDrawer addProperties(Map<String, String> properties) {
    mMenuDrawer.addItems(new DividerDrawerItem());
    for (Map.Entry<String, String> entry : properties.entrySet()) {
      mMenuDrawer.addItems(new SecondaryDrawerItem().withName(entry.getKey())
          .withIcon(android.R.drawable.ic_menu_info_details)
          .withDescription(entry.getValue())
          .withSelectable(false));
    }

    return this;
  }

  public void openDrawer() {
    mMenuDrawer.openDrawer();
  }

  private void addToggleDrawerItem(String text, int id) {
    ToggleDrawerItem item = new ToggleDrawerItem().withName(text).withIdentifier(id);
    item.setOnCheckedChangeListener(this);
    mMenuDrawer.addItems(item);
  }

  @Override
  public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView,
      boolean isChecked) {
    AppCompatActivity activity = mActivityViewWeakReference.get();
    Application application = mApplicationWeakReference.get();
    if (activity == null || application == null) {
      return;
    }

    int identifier = drawerItem.getIdentifier();

    if (identifier == R.id.drawer_dev_item_scalpel) {
      ToggleDrawerItem toggleDrawerItem = (ToggleDrawerItem) drawerItem;
      if (activity instanceof ScalpeledActivity) {
        ScalpeledActivity scalpeledActivity = (ScalpeledActivity) activity;
        ScalpelFrameLayout scalpelFrameLayout = scalpeledActivity.getScalpelFrameLayout();
        if (scalpelFrameLayout != null) {
          boolean enabled = toggleDrawerItem.isChecked();
          scalpelFrameLayout.setLayerInteractionEnabled(enabled);
          scalpelFrameLayout.setDrawViews(enabled);
          scalpelFrameLayout.setChromeShadowColor(android.R.color.black);
        }
      } else {
        showDialog(
            "The activity dont implements ScalpeledActivity interface, then cant use this option.");
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
      Context context = activity.getApplicationContext();
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

  private void showDialog(String dialog) {
    AppCompatActivity activity = mActivityViewWeakReference.get();
    if (activity == null) {
      return;
    }

    Toast.makeText(activity, dialog, Toast.LENGTH_LONG).show();
  }
}

