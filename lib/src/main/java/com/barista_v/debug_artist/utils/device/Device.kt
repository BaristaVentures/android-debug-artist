package com.barista_v.debug_artist.utils.device

interface Device {
  @Throws(Exception::class)
  fun takeScreenshot(fileName: String): String

  fun readLogFile(): String
}