package debug_artist.menu.drawer


interface Actor {
  fun enableLeakCanary()
  fun enablePicassoLogs()
  fun enableStetho()
  fun enableLynx()
  fun triggerAppRebirth()
}