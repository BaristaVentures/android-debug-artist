package com.barista_v.debug_artist.repositories

import android.os.Parcelable
import rx.Observable

interface BugReportRepository {
  fun createBug(name: String, description: String): Observable<Answer<Any>>

  interface Builder : Parcelable {
    fun build(): BugReportRepository
  }
}