package debug_artist.menu.drawer

import android.support.v4.app.FragmentActivity
import debug_artist.menu.report_bug.ReportBugActivity
import debug_artist.menu.report_bug.BugRepository

interface Traveler {
  fun startBugReportView(builder: BugRepository.Builder, screenshotFilePath: String, logsFilePath: String)
}

class DebugDrawerTraveler(val activity: FragmentActivity) : Traveler {

  override fun startBugReportView(builder: BugRepository.Builder, screenshotFilePath: String, logsFilePath: String) =
      activity.startActivity(ReportBugActivity.getInstance(activity, builder, screenshotFilePath, logsFilePath))

}