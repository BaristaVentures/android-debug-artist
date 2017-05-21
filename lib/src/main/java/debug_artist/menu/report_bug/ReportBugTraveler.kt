package debug_artist.menu.report_bug

import android.app.Activity


interface ReportBugTraveler {
  fun close()
}

class ReportBugTravelerImpl(val activity: Activity) : ReportBugTraveler {

  override fun close() = activity.finish()

}