package com.barista_v.debug_artist.drawer

import android.support.v4.app.FragmentActivity
import com.barista_v.debug_artist.report_bug.ReportBugActivity
import com.barista_v.debug_artist.repositories.BugRepository

interface Traveler {
  fun startBugReportView(builder: BugRepository.Builder, screenshotFilePath: String, logsFilePath: String)
}

class DebugDrawerTraveler(val activity: FragmentActivity) : Traveler {

  override fun startBugReportView(builder: BugRepository.Builder, screenshotFilePath: String, logsFilePath: String) =
      activity.startActivity(ReportBugActivity.getInstance(activity, builder, screenshotFilePath, logsFilePath))

}