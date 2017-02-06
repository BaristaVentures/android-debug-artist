package debug_artist.menu.utils.device

interface Device {
  @Throws(Exception::class)
  fun takeScreenshot(fileName: String): String

  fun readLogFile(): String
}