package com.barista_v.debug_artist.utils

interface Device {
  fun takeScreenshot(fileName: String): String
  fun readLogFile(): String
}