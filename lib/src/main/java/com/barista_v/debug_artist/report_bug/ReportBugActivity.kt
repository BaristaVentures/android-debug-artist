package com.barista_v.debug_artist.report_bug

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.barista_v.debug_artist.R
import com.barista_v.debug_artist.repositories.BugReportRepository

class ReportBugActivity : AppCompatActivity() {

  companion object {
    fun getInstance(activity: FragmentActivity, repositoryBuilder: BugReportRepository.Builder) =
        ExtrasHandler.getInstance(activity, repositoryBuilder)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.report_bug_activity_layout)
    (findViewById(R.id.toolbar) as? Toolbar)?.let { setSupportActionBar(it) }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

}