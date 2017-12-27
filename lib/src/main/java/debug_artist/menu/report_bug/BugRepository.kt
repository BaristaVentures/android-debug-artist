package debug_artist.menu.report_bug

import android.os.Parcelable
import io.reactivex.Observable

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