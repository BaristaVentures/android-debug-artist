package com.barista_v.debug_artist.report_bug

interface ReportBugView {
  fun showProgressDialog()
  fun dismissProgressDialog()
  fun showSuccessToast()
  fun showErrorDialog(text: String)
}