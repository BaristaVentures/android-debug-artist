package com.barista_v.debug_artist.item

import com.barista_v.debug_artist.item.input.InputItemListener
import com.barista_v.debug_artist.item.phoenix.RestartListener
import com.barista_v.debug_artist.item.spinner.SpinnerItemListener
import com.jakewharton.scalpel.ScalpelFrameLayout

interface MenuItem
open class DividerMenuItem : MenuItem

open class SwitchMenuItem(val checked: Boolean = false) : MenuItem
open class ButtonMenuItem : MenuItem
open class LabelMenuItem(val properties: Map<String, String>) : MenuItem

class StethoSwitchMenuItem(checked: Boolean = false) : SwitchMenuItem(checked)
class LeakCanarySwitchMenuItem(checked: Boolean = false) : SwitchMenuItem(checked)
class PicassoLogsSwitchMenuItem(checked: Boolean = false) : SwitchMenuItem(checked)
class ScalpelSwitchMenuItem(val layout: ScalpelFrameLayout,
                            checked: Boolean = false) : SwitchMenuItem(checked)

class LynksButtonMenuItem : ButtonMenuItem()

class PhoenixButtonMenuItem(val restartListener: RestartListener) : ButtonMenuItem()

class SpinnerMenuItem(val id: Int,
                      val name: String,
                      val options: Array<String>,
                      val selectedItem: Int,
                      val listener: SpinnerItemListener) : ButtonMenuItem()

class InputMenuItem(val id: Int,
                    val name: String,
                    val inputItemListener: InputItemListener) : ButtonMenuItem()