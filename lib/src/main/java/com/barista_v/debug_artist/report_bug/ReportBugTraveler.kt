package com.barista_v.debug_artist.report_bug

import android.support.v4.app.FragmentActivity

interface ReportBugTraveler {
  fun close()
}

class ReportBugTravelerImpl(val activity: FragmentActivity) : ReportBugTraveler {

  override fun close() = activity.finish()

}