package com.barista_v.debug_artist.drawer

interface Actor {
  fun enableLeakCanary()
  fun enablePicassoLogs()
  fun enableScalpelLayout()
  fun disableScalpelLayout()
  fun enableStetho()
  fun enableLynx()
  fun triggerAppRebirth()
}