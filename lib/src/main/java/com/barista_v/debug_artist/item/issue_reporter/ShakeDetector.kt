package com.barista_v.debug_artist.item.issue_reporter

interface ShakeDetector {
  fun start(listener: OnShakeListener)
  fun pause()
}

interface OnShakeListener {

  /**
   * @param count number of shakes that were made one after another.
   */
  fun onShake(count: Int)
}