package com.barista_v.debug_artist.repositories

import retrofit2.Response
import rx.Observable

interface BugReportRepository {
  fun createBug(name: String, description: String): Observable<Response<Any>>
}