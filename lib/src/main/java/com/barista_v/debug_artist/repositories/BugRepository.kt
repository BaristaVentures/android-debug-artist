package com.barista_v.debug_artist.repositories

import android.os.Parcelable
import rx.Observable

interface BugRepository {

  /**
   * Creates a bug
   */
  fun create(name: String,
             description: String,
             screenshotFilePath: String?,
             logsFilePath: String?): Observable<Answer<Any>>

  interface Builder : Parcelable {
    fun build(): BugRepository
  }

}