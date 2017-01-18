package com.barista_v.debug_artist.drawer

import android.support.v7.app.AppCompatActivity
import com.jraska.falcon.Falcon
import java.io.File

class ScreenshotPicker(val activity: AppCompatActivity) {

  fun take(fileName: String): String {
    val outFile = File(activity.cacheDir, fileName)
    Falcon.takeScreenshot(activity, outFile)
    return outFile.path
  }

}