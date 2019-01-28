package debug_artist.menu.drawer.item

import debug_artist.menu.drawer.item.input.InputItemListener
import debug_artist.menu.drawer.item.spinner.SpinnerItemListener

open class MenuItem
open class DividerMenuItem : MenuItem()

open class ButtonMenuItem : MenuItem()
open class LabelMenuItem(val properties: Map<String, String>) : MenuItem()

open class SpinnerMenuItem(val id: Int,
                           val name: String,
                           val options: Array<String>,
                           val selectedItem: Int,
                           val listener: SpinnerItemListener) : ButtonMenuItem()

open class InputMenuItem(val id: Int,
                         val name: String, val inputItemListener: InputItemListener) : ButtonMenuItem()