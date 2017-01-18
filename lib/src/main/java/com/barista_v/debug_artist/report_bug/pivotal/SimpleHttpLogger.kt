package com.barista_v.debug_artist.report_bug.pivotal

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

class SimpleHttpLogger(val tag: String) : HttpLoggingInterceptor.Logger {
  override fun log(message: String?) {
    Log.d(tag, message)
  }
}