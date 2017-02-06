package debug_artist.menu.drawer

import com.jakewharton.scalpel.ScalpelFrameLayout

interface Actor {
  var scalpelFrameLayout: ScalpelFrameLayout?

  fun enableLeakCanary()
  fun enablePicassoLogs()
  fun enableScalpelLayout()
  fun disableScalpelLayout()
  fun enableStetho()
  fun enableLynx()
  fun triggerAppRebirth()
}