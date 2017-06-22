package debug_artist.sample.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import debug_artist.sample.BaseActivity;
import debug_artist.sample.R;

public class BasicActivity extends BaseActivity {

  public static BasicActivity activityLeaked;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic);
    setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // Intentionally to leak memory and see it with leak canary
    activityLeaked = this;

    // Check BaseActivity for Debug Artist Initialization
  }
}
