package com.barista_v.debug_artist.drawer

import android.support.annotation.VisibleForTesting
import com.barista_v.debug_artist.item.*
import com.barista_v.debug_artist.item.input.InputItemListener
import com.barista_v.debug_artist.item.phoenix.RestartListener
import com.mikepenz.materialdrawer.model.DividerDrawerItem

class DebugDrawerPresenter {

  @VisibleForTesting internal var view: DebugDrawerView? = null
  @VisibleForTesting internal var actor: Actor? = null

  var restartListener: RestartListener? = null
  var inputItemListener: InputItemListener? = null

  fun onAttach(view: DebugDrawerView, actor: Actor) {
    this.view = view
    this.actor = actor
  }

  fun onDetach() {
    view = null
    actor = null
    restartListener = null
    inputItemListener = null
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
      is SpinnerMenuItem -> view?.addSpinnerItem(item)
      is LabelMenuItem -> item.properties.forEach { view?.addLabelItem(it.key, it.value) }
    }
  }

}