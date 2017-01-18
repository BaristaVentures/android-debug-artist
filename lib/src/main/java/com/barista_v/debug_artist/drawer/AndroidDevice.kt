package com.barista_v.debug_artist.drawer

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.jraska.falcon.Falcon
import java.io.*


class AndroidDevice(val activity: AppCompatActivity) {

  fun takeScreenshot(fileName: String): String {
    val outFile = File(activity.cacheDir, fileName)
    Falcon.takeScreenshot(activity, outFile)
    return outFile.path
  }

  /**
   * Source: http://stackoverflow.com/a/22174245/273119
   */
  fun readLogFile(): String {
    val fullName = "appLog.log"
    val file = File(activity.cacheDir, fullName)
    val pid = android.os.Process.myPid().toString()

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
      Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
    }

    try {
      Runtime.getRuntime().exec("logcat -c")
    } catch (e: IOException) {
      Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
    }

    return file.path
  }
}
