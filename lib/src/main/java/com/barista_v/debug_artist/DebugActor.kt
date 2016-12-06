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

  val applicationWeakReference = WeakReference(application)
  val activityWeakReference = WeakReference(activity)
  var scalpelFrameLayout: ScalpelFrameLayout? = null

  override fun enableLeakCanary() {
    applicationWeakReference.get()?.let {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      if (LeakCanary.isInAnalyzerProcess(it)) return

      LeakCanary.install(it)
    }
  }

  override fun enablePicassoLogs() {
    applicationWeakReference.get()?.let {
      val stats = Picasso.with(it).apply {
        setIndicatorsEnabled(true)
        isLoggingEnabled = true
      }.snapshot

      Log.i("DEBUG", "Picasso stats:" + stats.toString())
    } ?: Log.e("DebugActor", "scalpelFrameLayout property is not set.")
  }

  override fun enableScalpelLayout() = enableScalpelLayout(true)

  override fun disableScalpelLayout() = enableScalpelLayout(false)

  override fun enableStetho() {
    activityWeakReference.get()?.let { Stetho.initializeWithDefaults(it) }
  }

  override fun enableLynx() {
    activityWeakReference.get()?.let {
      val intent = applicationWeakReference.get()?.let { LynxActivity.getIntent(it) }
      if (intent != null) {
        it.startActivity(intent)
      }
    }
  }

  override fun triggerAppRebirth() {
    applicationWeakReference.get()?.let { ProcessPhoenix.triggerRebirth(it) }
  }

  private fun enableScalpelLayout(enabled: Boolean) {
    scalpelFrameLayout?.apply {
      isLayerInteractionEnabled = enabled
      setDrawViews(enabled)
      chromeShadowColor = R.color.black
    } ?: Log.e("DebugActor", "scalpelFrameLayout property is not set.")
  }
}