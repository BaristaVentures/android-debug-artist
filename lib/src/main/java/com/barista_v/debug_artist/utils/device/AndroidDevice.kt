package com.barista_v.debug_artist.utils.device

import android.os.Process
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.jraska.falcon.Falcon
import java.io.*

/**
 * Know how to pick things from an Android Device.
 */
class AndroidDevice(val activity: AppCompatActivity) : Device {

  val TAG = "AndroidDevice"

  @Throws(Falcon.UnableToTakeScreenshotException::class)
  override fun takeScreenshot(fileName: String): String {
    val outFile = File(activity.cacheDir, fileName)
    Falcon.takeScreenshot(activity, outFile)
    return outFile.path
  }

  /**
   * Read logcat output, write in [AppCompatActivity.getCacheDir()].
   * @return path with the log file
   *
   * Source: http://stackoverflow.com/a/22174245/273119
   */
  override fun readLogFile(): String {
    val fullName = "appLog.log"
    val file = File(activity.cacheDir, fullName)
    val pid = Process.myPid().toString()

    if (file.exists()) {
      file.delete()
    }

    try {
      val command = String.format("logcat -d -v threadtime *:*")
      val process = Runtime.getRuntime().exec(command)

      val reader = BufferedReader(InputStreamReader(process.inputStream))
      val result = StringBuilder()

      var currentLine = reader.readLine()

      while (currentLine != null) {
        currentLine = reader.readLine()

        if (currentLine != null && currentLine.contains(pid)) {
          result.append(currentLine).append("\n")
        }
      }

      val out = FileWriter(file)
      out.write(result.toString())
      out.close()

    } catch (e: IOException) {
      Log.e(TAG, "Reading logcat output", e)
    }

    try {
      Runtime.getRuntime().exec("logcat -c")
    } catch (e: IOException) {
      Log.e(TAG, "Closing logcat (logcat -c)", e)
    }

    return file.path
  }
}
