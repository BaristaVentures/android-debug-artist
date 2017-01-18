package com.barista_v.debug_artist.repositories

import android.os.Parcelable
import rx.Observable

interface BugRepository {

  fun createBug(name: String,
                description: String,
                screenshotFilePath: String?,
                logsFilePath: String?): Observable<Answer<Any>>

  interface Builder : Parcelable {
    fun build(): BugRepository
  }

}