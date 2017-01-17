package com.barista_v.debug_artist.item

import com.barista_v.debug_artist.item.input.InputItemListener
import com.barista_v.debug_artist.item.phoenix.RestartListener
import com.barista_v.debug_artist.item.spinner.SpinnerItemListener
import com.barista_v.debug_artist.repositories.BugReportRepository
import com.jakewharton.scalpel.ScalpelFrameLayout

open class MenuItem
open class DividerMenuItem : MenuItem()

open class SwitchMenuItem(var checked: Boolean = false) : MenuItem()

open class ButtonMenuItem : MenuItem()
open class LabelMenuItem(val properties: Map<String, String>) : MenuItem()

open class StethoSwitchMenuItem(checked: Boolean = false) : SwitchMenuItem(checked)
open class LeakCanarySwitchMenuItem(checked: Boolean = false) : SwitchMenuItem(checked)
open class PicassoLogsSwitchMenuItem(checked: Boolean = false) : SwitchMenuItem(checked)

open class ScalpelSwitchMenuItem(val layout: ScalpelFrameLayout?, checked: Boolean = false)
  : SwitchMenuItem(checked)

open class LynksButtonMenuItem : ButtonMenuItem()

open class PhoenixButtonMenuItem(val restartListener: RestartListener) : ButtonMenuItem()

open class SpinnerMenuItem(val id: Int,
                           val name: String,
                           val options: Array<String>,
                           val selectedItem: Int,
                           val listener: SpinnerItemListener) : ButtonMenuItem()

open class InputMenuItem(val id: Int,
                         val name: String,
                         val inputItemListener: InputItemListener) : ButtonMenuItem()

open class ReportBugSwitchMenuItem(checked: Boolean = false,
                                   val bugReportRepository: BugReportRepository) : SwitchMenuItem(checked)