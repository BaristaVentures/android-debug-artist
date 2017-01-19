package com.barista_v.debug_artist.repositories.pivotal

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

class SimpleHttpLogger(val tag: String) : HttpLoggingInterceptor.Logger {
  override fun log(message: String?) {
    Log.d(tag, message)
  }
}