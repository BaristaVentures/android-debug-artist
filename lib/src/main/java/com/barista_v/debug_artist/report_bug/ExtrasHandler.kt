package com.barista_v.debug_artist.report_bug

import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.barista_v.debug_artist.repositories.BugReportRepository

class ExtrasHandler(intent: Intent) {

  companion object {
    val extraRepository = "report.repository"
    val extraScreenshotFilePath = "report.screenshot"

    fun getInstance(activity: FragmentActivity, repositoryBuilder: BugReportRepository.Builder,
                    screenshotFilePath: String?) =
        Intent(activity, ReportBugActivity::class.java).apply {
          putExtra(extraRepository, repositoryBuilder)
          screenshotFilePath?.let { putExtra(extraScreenshotFilePath, it) }
        }
  }

  val extraRepositoryBuilder: BugReportRepository.Builder =
      intent.extras.getParcelable(extraRepository)

  val filePath: String? = intent.extras.getString(extraScreenshotFilePath)
}