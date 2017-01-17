package com.barista_v.debug_artist.repositories

import rx.Observable

interface BugReportRepository {
  fun createBug(name: String, description: String): Observable<Answer<Any>>
}