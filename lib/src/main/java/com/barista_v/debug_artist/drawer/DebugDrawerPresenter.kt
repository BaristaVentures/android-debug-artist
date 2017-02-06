package com.barista_v.debug_artist.drawer

import android.support.annotation.VisibleForTesting
import com.barista_v.debug_artist.drawer.item.*
import com.barista_v.debug_artist.drawer.item.input.InputItemListener
import com.barista_v.debug_artist.drawer.item.phoenix.RestartListener
import com.barista_v.debug_artist.repositories.BugRepository
import com.barista_v.debug_artist.utils.device.Device
import com.barista_v.debug_artist.utils.shake_detector.OnShakeListener
import com.barista_v.debug_artist.utils.shake_detector.ShakeDetector
import com.mikepenz.materialdrawer.model.DividerDrawerItem

class DebugDrawerPresenter : OnShakeListener {

  @VisibleForTesting internal var view: DebugDrawerView? = null
  @VisibleForTesting internal var actor: Actor? = null
  @VisibleForTesting internal var bugRepositoryBuilder: BugRepository.Builder? = null
  @VisibleForTesting internal var shakeDetector: ShakeDetector? = null
  @VisibleForTesting internal var restartListener: RestartListener? = null
  @VisibleForTesting internal var inputItemListener: InputItemListener? = null

  private var traveler: Traveler? = null
  private var device: Device? = null

  fun attach(view: DebugDrawerView, traveler: Traveler,
             actor: Actor, shakeDetector: ShakeDetector, device: Device) {
    this.view = view
    this.traveler = traveler
    this.actor = actor
    this.shakeDetector = shakeDetector
    this.device = device
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
    startOrPauseShakeListener(checked)
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
        bugRepositoryBuilder = item.repositoryBuilder
        view?.addBugReportSwitch(item.checked)
        startOrPauseShakeListener(item.checked)
      }
      is SpinnerMenuItem -> view?.addSpinnerItem(item)
      is LabelMenuItem -> item.properties.forEach { view?.addLabelItem(it.key, it.value) }
    }
  }

  override fun onShake(count: Int) {
    if (count > 1) return

    try {
      val screenshotPath = device?.takeScreenshot("screenshot.jpg") ?: return //TODO: log warning
      val logPath = device?.readLogFile() ?: return //TODO: log warning

      bugRepositoryBuilder?.let {
        traveler?.startBugReportView(it, screenshotPath, logPath)
      } //TODO: log warning
    } catch (e: Exception) {
      val message = "message= ${e.message} \ncause=${e.cause?.message}"
      view?.showErrorDialog(message)
    }
  }

  private fun startOrPauseShakeListener(checked: Boolean){
    if (checked) {
      shakeDetector?.start(this@DebugDrawerPresenter)
    } else {
      shakeDetector?.pause()
    }
  }


}