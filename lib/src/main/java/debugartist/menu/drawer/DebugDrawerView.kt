package debugartist.menu.drawer

import debugartist.menu.drawer.item.InputMenuItem
import debugartist.menu.drawer.item.SpinnerMenuItem

interface DebugDrawerView {
  fun addDividerItem()

  fun addInputItem(item: InputMenuItem)
  fun addSpinnerItem(it: SpinnerMenuItem)
  fun addLabelItem(id: String, text: String)

  fun showProgressDialog()
  fun dismissProgressDialog()
  fun showErrorDialog(message: String)
  fun showSuccessToast()
}