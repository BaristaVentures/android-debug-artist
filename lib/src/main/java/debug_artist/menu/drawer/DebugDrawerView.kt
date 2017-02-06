package debug_artist.menu.drawer

import debug_artist.menu.drawer.item.InputMenuItem
import debug_artist.menu.drawer.item.SpinnerMenuItem

interface DebugDrawerView {
  fun addDividerItem()
  fun addStethoSwitch(checked: Boolean)
  fun addLeakCanarySwitch(checked: Boolean)
  fun addPicassoLogsSwitch(checked: Boolean)
  fun addScalpelSwitch(checked: Boolean)
  fun addBugReportSwitch(checked: Boolean)

  fun addLynksButton()
  fun addPhoenixButton()

  fun addInputItem(item: InputMenuItem)
  fun addSpinnerItem(it: SpinnerMenuItem)
  fun addLabelItem(id: String, text: String)

  fun showProgressDialog()
  fun dismissProgressDialog()
  fun showErrorDialog(message: String)
  fun showSuccessToast()
}