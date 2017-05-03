package debug_artist.menu.utils.device

import android.app.Activity
import android.os.Build
import android.os.Process
import android.util.Log
import com.jraska.falcon.Falcon
import java.io.*

/**
 * Know how to pick things from an Android Device.
 */
class AndroidDevice(val activity: Activity) {

  val TAG = "AndroidDevice"

  companion object {

    /**
     * Return a map with keys and values referencing environment variables like
     * app version, android version, model, etc...
     */
    @JvmStatic
    @JvmOverloads
    fun getProjectProperties(activity: Activity, extraProperties: HashMap<String, String>? = null)
        : MutableMap<String, String> {
      return activity.packageManager.getPackageInfo(activity.packageName, 0).let {
        mutableMapOf<String, String>(
            "AppVersion" to it.versionName,
            "Build" to it.versionCode.toString(),
            "AndroidVersion" to Build.VERSION.RELEASE,
            "Manufacturer" to Build.MANUFACTURER,
            "Model" to Build.MODEL).apply {
          extraProperties?.let { putAll(extraProperties) }
        }
      }
    }
  }

  @Throws(Falcon.UnableToTakeScreenshotException::class)
  fun takeScreenshot(fileName: String): String {
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
  fun readLogFile(): String {
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
