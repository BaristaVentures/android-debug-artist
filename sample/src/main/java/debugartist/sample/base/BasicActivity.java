package debugartist.sample.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import debugartist.sample.BaseActivity;
import debugartist.sample.R;

public class BasicActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic);
    setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // Check BaseActivity for Debug Artist Initialization
  }
}
