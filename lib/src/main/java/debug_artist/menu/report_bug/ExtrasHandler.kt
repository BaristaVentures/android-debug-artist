package debug_artist.menu.report_bug

import android.app.Activity
import android.content.Intent
import debug_artist.menu.report_bug.ReportBugActivity

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

    fun getInstance(activity: Activity, repositoryBuilder: BugRepository.Builder,
                    screenshotFilePath: String, logsFilePath: String) =
        Intent(activity, ReportBugActivity::class.java).apply {
          putExtra(ExtrasHandlerImpl.Companion.extraRepository, repositoryBuilder)
          putExtra(ExtrasHandlerImpl.Companion.extraScreenshot, screenshotFilePath)
          putExtra(ExtrasHandlerImpl.Companion.extraLogs, logsFilePath)
        }
  }

  override val extraRepositoryBuilder: BugRepository.Builder =
      intent.extras.getParcelable(ExtrasHandlerImpl.Companion.extraRepository)

  override val screenshotFilePath: String = intent.extras.getString(ExtrasHandlerImpl.Companion.extraScreenshot)

  override val logsFilePath: String = intent.extras.getString(ExtrasHandlerImpl.Companion.extraLogs)
}