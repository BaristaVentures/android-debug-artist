package com.barista_v.debug_artist.report_bug

import android.util.Log
import com.barista_v.debug_artist.extensions.composeForIoTasks
import com.barista_v.debug_artist.repositories.BugReportRepository

class ReportBugPresenter {
  val TAG = "ReportBugView"

  private var view: ReportBugView? = null
  private var bugReportRepository: BugReportRepository? = null

  fun attach(view: ReportBugView, extrasHandler: ExtrasHandler) {
    this.view = view
    this.bugReportRepository = extrasHandler.extraRepositoryBuilder.build()
  }

  fun onSendButtonClick(name: String, description: String) {
    view?.showProgressDialog()

    bugReportRepository?.createBug(name, description)
        ?.composeForIoTasks()
        ?.doOnTerminate { view?.dismissProgressDialog() }
        ?.subscribe({
          if (it.error == null) {
            view?.showSuccessToast()
          } else {
            view?.showErrorDialog(it.error.cause.toString())
          }
        }, {
          view?.showErrorDialog(it.message ?: "Something happened")
        })
        ?: Log.w(TAG, "You need to set BugReportRepository first before.")
  }

}