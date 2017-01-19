package com.barista_v.debug_artist.utils.device

interface Device {
  fun takeScreenshot(fileName: String): String
  fun readLogFile(): String
}