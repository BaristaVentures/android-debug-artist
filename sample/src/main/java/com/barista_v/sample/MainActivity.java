package com.barista_v.sample;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.barista_v.debug_artist.ViewServer;

public class MainActivity extends MyActivity {

  // Memory leak 1
  public static MainActivity activityLeaked;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Memory leak 2
    activityLeaked = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
    }


  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ViewServer.get(this).removeWindow(this);
  }


}
