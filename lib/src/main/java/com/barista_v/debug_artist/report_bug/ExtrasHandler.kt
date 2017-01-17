package com.barista_v.debug_artist.report_bug

import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.barista_v.debug_artist.repositories.BugReportRepository

internal class ExtrasHandler(intent: Intent) {

  companion object {
    val extraRepository = "report.repository"

    fun getInstance(activity: FragmentActivity, repositoryBuilder: BugReportRepository.Builder) =
        Intent(activity, ReportBugActivity::class.java).apply {
          putExtra(extraRepository, repositoryBuilder)
        }
  }

  val extraRepositoryBuilder: BugReportRepository.Builder =
      intent.extras.getParcelable(extraRepository)
}