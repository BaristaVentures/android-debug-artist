package com.barista_v.debug_artist.report_bug

import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.barista_v.debug_artist.repositories.BugRepository

open class ExtrasHandler(intent: Intent) {

  companion object {
    val extraRepository = "report.repository"
    val extraScreenshot = "report.screenshot"
    val extraLogs = "report.logs"

    fun getInstance(activity: FragmentActivity, repositoryBuilder: BugRepository.Builder,
                    screenshotFilePath: String, logsFilePath: String) =
        Intent(activity, ReportBugActivity::class.java).apply {
          putExtra(extraRepository, repositoryBuilder)
          putExtra(extraScreenshot, screenshotFilePath)
          putExtra(extraLogs, logsFilePath)
        }
  }

  val extraRepositoryBuilder: BugRepository.Builder =
      intent.extras.getParcelable(extraRepository)

  val screenshotFilePath: String = intent.extras.getString(extraScreenshot)

  val logsFilePath: String = intent.extras.getString(extraLogs)
}