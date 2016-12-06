package com.barista_v.debug_artist.drawer

import android.support.annotation.VisibleForTesting
import com.barista_v.debug_artist.item.input.InputItemListener
import com.barista_v.debug_artist.item.phoenix.RestartListener

class DebugDrawerPresenter() {

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

  fun onLeakCanarySwitchAdded(checked: Boolean) {
    if (checked) {
      actor?.enableLeakCanary()
    }
  }

}