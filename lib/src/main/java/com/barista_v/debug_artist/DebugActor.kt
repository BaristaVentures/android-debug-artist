package com.barista_v.debug_artist

import android.app.Activity
import android.app.Application
import android.util.Log
import com.barista_v.debug_artist.drawer.Actor
import com.facebook.stetho.Stetho
import com.github.pedrovgs.lynx.LynxActivity
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.scalpel.ScalpelFrameLayout
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
import java.lang.ref.WeakReference

class DebugActor(application: Application, activity: Activity) : Actor {

  val TAG = "DebugActor"
  val applicationWeakReference = WeakReference(application)
  val activityWeakReference = WeakReference(activity)
  override var scalpelFrameLayout: ScalpelFrameLayout? = null

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

  override fun enableScalpelLayout() = enableScalpelLayout(true)

  override fun disableScalpelLayout() = enableScalpelLayout(false)

  override fun enableStetho() {
    activityWeakReference.get()?.let {
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

  private fun enableScalpelLayout(enabled: Boolean) {
    scalpelFrameLayout?.apply {
      isLayerInteractionEnabled = enabled
      setDrawViews(enabled)
      chromeShadowColor = R.color.black

      Log.i(TAG, "Scalpel Layout enabled:" + if (enabled) "true" else "false")
    } ?: Log.e(TAG, "scalpelFrameLayout property is not set.")
  }
}