package com.barista_v.sample;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.barista_v.debug_artist.ViewServer;

public class MainActivity extends MyActivity {

  public static MainActivity activityLeaked;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Intentionally to leak memory and see it with leak canary
    activityLeaked = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
    }
  }

}
