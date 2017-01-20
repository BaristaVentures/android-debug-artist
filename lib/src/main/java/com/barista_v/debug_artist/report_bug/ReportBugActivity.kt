package com.barista_v.debug_artist.report_bug

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.barista_v.debug_artist.R
import com.barista_v.debug_artist.repositories.BugRepository
import java.lang.ref.WeakReference

class ReportBugActivity : AppCompatActivity(), ReportBugView {

  companion object {
    fun getInstance(activity: FragmentActivity,
                    repositoryBuilder: BugRepository.Builder,
                    screenshotFilePath: String, logsFilePath: String) =
        ExtrasHandlerImpl.getInstance(activity, repositoryBuilder, screenshotFilePath, logsFilePath)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.report_bug_activity_layout)

    (findViewById(R.id.toolbar) as? Toolbar)?.let { setSupportActionBar(it) }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    val presenter = ReportBugPresenter().apply {
      attach(this@ReportBugActivity,
          ReportBugTravelerImpl(this@ReportBugActivity),
          ExtrasHandlerImpl(intent))
    }

    findViewById(R.id.sendButton).setOnClickListener {
      presenter.onSendButtonClick((findViewById(R.id.titleEditText) as EditText).text.toString(),
          (findViewById(R.id.descriptionEditText) as EditText).text.toString())
    }

    (findViewById(R.id.descriptionEditText) as EditText).setOnEditorActionListener { textView, id, keyEvent ->
      if (id == EditorInfo.IME_NULL && keyEvent.action == KeyEvent.ACTION_DOWN) {
        presenter.onSendButtonClick((findViewById(R.id.titleEditText) as EditText).text.toString(),
            (findViewById(R.id.descriptionEditText) as EditText).text.toString())
        true
      } else {
        false
      }
    }
  }

  private var progressWeakReference = WeakReference<ProgressDialog>(null)

  override fun showProgressDialog() {
    dismissProgressDialog()

    val dialog = ProgressDialog(this).apply {
      setMessage(getString(R.string.loading))
      isIndeterminate = true
      setCancelable(false)
      show()
    }

    progressWeakReference = WeakReference(dialog)
  }

  override fun dismissProgressDialog() {
    progressWeakReference.get()?.apply { if (isShowing) dismiss() }
    progressWeakReference.clear()
  }

  override fun showSuccessToast() {
    Toast.makeText(this, R.string.success, Toast.LENGTH_LONG).show()
  }

  override fun showErrorDialog(text: String) {
    AlertDialog.Builder(this)
        .setTitle("Error")
        .setPositiveButton(android.R.string.ok, null)
        .setMessage(text)
        .show()
  }

  override fun setScreenshotImage(screenshotFilePath: String) {
    (findViewById(R.id.screenshotImageView) as ImageView).setImageURI(Uri.parse(screenshotFilePath))
  }
}