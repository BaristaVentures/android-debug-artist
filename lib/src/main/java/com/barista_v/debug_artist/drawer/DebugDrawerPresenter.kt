package com.barista_v.debug_artist.drawer

import android.support.annotation.VisibleForTesting
import com.barista_v.debug_artist.drawer.item.*
import com.barista_v.debug_artist.drawer.item.input.InputItemListener
import com.barista_v.debug_artist.drawer.item.issue_reporter.OnShakeListener
import com.barista_v.debug_artist.drawer.item.issue_reporter.ShakeDetector
import com.barista_v.debug_artist.drawer.item.phoenix.RestartListener
import com.barista_v.debug_artist.repositories.BugReportRepository
import com.mikepenz.materialdrawer.model.DividerDrawerItem

class DebugDrawerPresenter : OnShakeListener {
  val TAG = "DebugDrawerPresenter"

  @VisibleForTesting internal var view: DebugDrawerView? = null
  @VisibleForTesting internal var actor: Actor? = null
  @VisibleForTesting internal var bugRepositoryBuilder: BugReportRepository.Builder? = null
  @VisibleForTesting internal var shakeDetector: ShakeDetector? = null
  @VisibleForTesting internal var restartListener: RestartListener? = null
  @VisibleForTesting internal var inputItemListener: InputItemListener? = null

  private var traveler: DebugDrawerTraveler? = null
  private var screenshotPicker: ScreenshotPicker? = null

  fun attach(view: DebugDrawerView, traveler: DebugDrawerTraveler,
             actor: Actor, shakeDetector: ShakeDetector, screenshotPicker: ScreenshotPicker) {
    this.view = view
    this.traveler = traveler
    this.actor = actor
    this.shakeDetector = shakeDetector
    this.screenshotPicker = screenshotPicker
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
        shakeDetector?.start(this)
        bugRepositoryBuilder = item.repositoryBuilder
      }
      is SpinnerMenuItem -> view?.addSpinnerItem(item)
      is LabelMenuItem -> item.properties.forEach { view?.addLabelItem(it.key, it.value) }
    }
  }

  override fun onShake(count: Int) {
    if (count > 1) return

    bugRepositoryBuilder?.let {
      val screehshotPath = screenshotPicker?.take("screenshot.jpg")
      traveler?.startBugReportView(it, screehshotPath)
    }
  }

}