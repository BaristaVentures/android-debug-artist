package debug_artist.menu

import android.app.Activity
import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.github.pedrovgs.lynx.LynxActivity
import com.jakewharton.processphoenix.ProcessPhoenix
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
import debug_artist.menu.drawer.Actor
import java.lang.ref.WeakReference

class DebugActor(application: Application, activity: Activity) : Actor {

  companion object {
    private const val TAG = "DebugActor"
  }

  private val applicationWeakReference = WeakReference(application)
  private val activityWeakReference = WeakReference(activity)

  override fun enableLeakCanary() {
    applicationWeakReference.get()?.let {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      if (LeakCanary.isInAnalyzerProcess(it)) return

      LeakCanary.install(it)
      Log.i(TAG, "Leak Canary enabled")
    }
  }

  override fun enablePicassoLogs() {
    applicationWeakReference.get()?.let {
      val stats = Picasso.with(it).apply {
        setIndicatorsEnabled(true)
        isLoggingEnabled = true
      }.snapshot

      Log.i(TAG, "Picasso stats enabled: $stats")
    } ?: Log.e(TAG, "scalpelFrameLayout property is not set.")
  }

  override fun enableStetho() {
    applicationWeakReference.get()?.let {
      Stetho.initializeWithDefaults(it)
      Log.i(TAG, "Stetho enabled")
    }
  }

  override fun enableLynx() {
    activityWeakReference.get()?.let {
      val intent = applicationWeakReference.get()?.let { LynxActivity.getIntent(it) }
      if (intent != null) {
        it.startActivity(intent)
        Log.i(TAG, "Lynx enabled")
      }
    }
  }

  override fun triggerAppRebirth() {
    applicationWeakReference.get()?.let {
      ProcessPhoenix.triggerRebirth(it)
      Log.i(TAG, "App Rebirth enabled")
    }
  }

}