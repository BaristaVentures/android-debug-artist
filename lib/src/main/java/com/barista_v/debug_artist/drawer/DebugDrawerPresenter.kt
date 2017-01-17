package com.barista_v.debug_artist.drawer

import android.support.annotation.VisibleForTesting
import android.util.Log
import com.barista_v.debug_artist.extensions.composeForIoTasks
import com.barista_v.debug_artist.item.*
import com.barista_v.debug_artist.item.input.InputItemListener
import com.barista_v.debug_artist.item.issue_reporter.OnShakeListener
import com.barista_v.debug_artist.item.issue_reporter.ShakeDetector
import com.barista_v.debug_artist.item.phoenix.RestartListener
import com.barista_v.debug_artist.repositories.BugReportRepository
import com.mikepenz.materialdrawer.model.DividerDrawerItem

class DebugDrawerPresenter : OnShakeListener {
  val TAG = "DebugDrawerPresenter"

  @VisibleForTesting internal var view: DebugDrawerView? = null
  @VisibleForTesting internal var actor: Actor? = null
  @VisibleForTesting internal var bugReportRepository: BugReportRepository? = null
  @VisibleForTesting internal var shakeDetector: ShakeDetector? = null

  var restartListener: RestartListener? = null
  var inputItemListener: InputItemListener? = null

  fun onAttach(view: DebugDrawerView, actor: Actor, shakeDetector: ShakeDetector) {
    this.view = view
    this.actor = actor
    this.shakeDetector = shakeDetector
  }

  fun deAttach() {
    view = null
    actor = null
    restartListener = null
    inputItemListener = null

    shakeDetector?.pause()
  }

  fun onPicassoItemSelected() {
    actor?.enablePicassoLogs()
  }

  fun onLeakCanaryItemSelected() {
    actor?.enableLeakCanary()
  }

  fun onStethoItemSelected() {
    actor?.enableStetho()
  }

  fun onBugReporterItemSelected(checked: Boolean) {
    if (checked) {
      shakeDetector?.start(this@DebugDrawerPresenter)
    } else {
      shakeDetector?.pause()
    }
  }

  fun onScalpelItemSelected(enabled: Boolean) {
    if (enabled) {
      actor?.enableScalpelLayout()
    } else {
      actor?.disableScalpelLayout()
    }
  }

  fun onPhoenixItemSelected() {
    actor?.triggerAppRebirth()
    restartListener?.onAppRestarted()
  }

  fun onLynksItemSelected() {
    actor?.enableLynx()
  }

  fun onTextInputEntered(id: Int, text: String) {
    inputItemListener?.onTextInputEnter(id, text)
  }

  fun onItemAdded(item: MenuItem) {
    when (item) {
      is DividerDrawerItem -> view?.addDividerItem()
      is StethoSwitchMenuItem -> {
        view?.addStethoSwitch(item.checked)
        if (item.checked) actor?.enableStetho()
      }
      is LeakCanarySwitchMenuItem -> {
        view?.addLeakCanarySwitch(item.checked)
        if (item.checked) actor?.enableLeakCanary()
      }
      is PicassoLogsSwitchMenuItem -> {
        view?.addPicassoLogsSwitch(item.checked)
        if (item.checked) actor?.enablePicassoLogs()
      }
      is ScalpelSwitchMenuItem -> {
        view?.addScalpelSwitch(item.checked)
        actor?.scalpelFrameLayout = item.layout

        if (item.checked) {
          actor?.enableScalpelLayout()
        }
      }
      is LynksButtonMenuItem -> view?.addLynksButton()
      is PhoenixButtonMenuItem -> {
        view?.addPhoenixButton()
        restartListener = item.restartListener
      }
      is InputMenuItem -> {
        view?.addInputItem(item)
        inputItemListener = item.inputItemListener
      }
      is ReportBugSwitchMenuItem -> {
        view?.addBugReportSwitch(item.checked)
        bugReportRepository = item.bugReportRepository
      }
      is SpinnerMenuItem -> view?.addSpinnerItem(item)
      is LabelMenuItem -> item.properties.forEach { view?.addLabelItem(it.key, it.value) }
    }
  }

  override fun onShake(count: Int) {
    if (count > 1) return

    view?.showProgressDialog()

    bugReportRepository?.createBug("Bug found", "Some descr")
        ?.composeForIoTasks()
        ?.doOnTerminate { view?.dismissProgressDialog() }
        ?.subscribe({
          if (it.error == null) {
            view?.showSuccessToast()
          } else {
            view?.showErrorDialog(it.error.cause.toString())
          }
        }, {
          view?.showErrorDialog(it.message ?: "Something hapened")
        })
        ?: Log.w(TAG, "You need to set BugReportRepository first before.")
  }

}