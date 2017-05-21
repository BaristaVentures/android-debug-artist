package debug_artist.menu.drawer

import android.app.Activity
import debug_artist.menu.report_bug.ReportBugActivity
import debug_artist.menu.report_bug.BugRepository

interface Traveler {
  fun startBugReportView(builder: BugRepository.Builder, screenshotFilePath: String, logsFilePath: String)
}

class DebugDrawerTraveler(val activity: Activity) : Traveler {

  override fun startBugReportView(builder: BugRepository.Builder, screenshotFilePath: String, logsFilePath: String) =
      activity.startActivity(ReportBugActivity.getInstance(activity, builder, screenshotFilePath, logsFilePath))

}