package debugartist.menu.drawer

import android.support.annotation.VisibleForTesting
import debugartist.menu.drawer.item.*
import debugartist.menu.drawer.item.input.InputItemListener
import debugartist.menu.utils.device.AndroidDevice

class DebugDrawerPresenter {

  @VisibleForTesting
  internal var view: DebugDrawerView? = null
  @VisibleForTesting
  internal var inputItemListener: InputItemListener? = null

  private var device: AndroidDevice? = null

  fun attach(view: DebugDrawerView, device: AndroidDevice) {
    this.view = view
    this.device = device
  }

  fun deAttach() {
    view = null
    inputItemListener = null
  }

  fun onTextInputEntered(id: Int, text: String) {
    inputItemListener?.onTextInputEnter(id, text)
  }

  fun onItemAdded(item: MenuItem) {
    when (item) {
      is DividerMenuItem -> view?.addDividerItem()
      is InputMenuItem -> {
        view?.addInputItem(item)
        inputItemListener = item.inputItemListener
      }
      is SpinnerMenuItem -> view?.addSpinnerItem(item)
      is LabelMenuItem -> item.properties.forEach { view?.addLabelItem(it.key, it.value) }
    }
  }

}