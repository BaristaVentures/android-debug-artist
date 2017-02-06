package debug_artist.sample;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends MyActivity {

  public static MainActivity activityLeaked;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

    // Intentionally to leak memory and see it with leak canary
    activityLeaked = this;
  }
}
