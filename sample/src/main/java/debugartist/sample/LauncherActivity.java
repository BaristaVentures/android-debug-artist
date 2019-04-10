package debugartist.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import debugartist.sample.base.BasicActivity;

public class LauncherActivity extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launcher);
    setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

    findViewById(R.id.basicButton).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(LauncherActivity.this, BasicActivity.class));
      }
    });
  }
}
