package com.barista_v.debug_artist.drawer

import android.support.v4.app.FragmentActivity
import com.barista_v.debug_artist.report_bug.ReportBugActivity
import com.barista_v.debug_artist.repositories.BugReportRepository

open class DebugDrawerTraveler(val activity: FragmentActivity) {

  fun startBugReportView(builder: BugReportRepository.Builder, screenshotFilePath: String, logsFilePath: String) =
      activity.startActivity(ReportBugActivity.getInstance(activity, builder, screenshotFilePath, logsFilePath))

}