package debug_artist.menu.report_bug

interface ReportBugView {
  fun showProgressDialog()
  fun dismissProgressDialog()
  fun showSuccessToast()
  fun showErrorDialog(text: String)
  fun setScreenshotImage(screenshotFilePath: String)
}