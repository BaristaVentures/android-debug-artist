package com.barista_v.debug_artist.report_bug

import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.barista_v.debug_artist.repositories.BugRepository

interface ExtrasHandler {
  val extraRepositoryBuilder: BugRepository.Builder
  val screenshotFilePath: String
  val logsFilePath: String
}

class ExtrasHandlerImpl(intent: Intent) : ExtrasHandler {

  companion object {
    internal val extraRepository = "report.repository"
    internal val extraScreenshot = "report.screenshot"
    internal val extraLogs = "report.logs"

    fun getInstance(activity: FragmentActivity, repositoryBuilder: BugRepository.Builder,
                    screenshotFilePath: String, logsFilePath: String) =
        Intent(activity, ReportBugActivity::class.java).apply {
          putExtra(extraRepository, repositoryBuilder)
          putExtra(extraScreenshot, screenshotFilePath)
          putExtra(extraLogs, logsFilePath)
        }
  }

  override val extraRepositoryBuilder: BugRepository.Builder =
      intent.extras.getParcelable(extraRepository)

  override val screenshotFilePath: String = intent.extras.getString(extraScreenshot)

  override val logsFilePath: String = intent.extras.getString(extraLogs)
}