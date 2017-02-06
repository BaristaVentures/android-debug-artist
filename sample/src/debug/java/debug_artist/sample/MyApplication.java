package debug_artist.sample;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

public class MyApplication extends Application {

  /**
   * Should only be used in debug builds (do not save application statically)
   */
  public static MyApplication sInstance;

  @Override
  public void onCreate() {
    super.onCreate();

    // Dont initialize anything in this process since is recreated by LeakCanary
    if (LeakCanary.isInAnalyzerProcess(this)) return;

    //
    // "Setup firebase"
    //

    sInstance = this;
  }
}
