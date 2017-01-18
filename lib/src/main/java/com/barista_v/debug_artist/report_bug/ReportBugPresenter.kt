package com.barista_v.debug_artist.report_bug

import android.util.Log
import com.barista_v.debug_artist.extensions.composeForIoTasks
import com.barista_v.debug_artist.repositories.BugReportRepository

class ReportBugPresenter() {
  val TAG = "ReportBugView"

  private var view: ReportBugView? = null
  private var bugReportRepository: BugReportRepository? = null
  private var extrasHandler: ExtrasHandler? = null

  fun attach(view: ReportBugView, extrasHandler: ExtrasHandler) {
    this.view = view
    this.extrasHandler = extrasHandler
    this.bugReportRepository = extrasHandler.extraRepositoryBuilder.build()
  }

  fun onSendButtonClick(name: String, description: String) {
    view?.showProgressDialog()

    val createBugObservable = bugReportRepository?.createBug(name, description,
        extrasHandler?.screenshotFilePath, extrasHandler?.logsFilePath)

    createBugObservable?.composeForIoTasks()
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