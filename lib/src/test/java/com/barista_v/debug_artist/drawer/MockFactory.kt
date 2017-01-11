package com.barista_v.debug_artist.drawer

import com.barista_v.debug_artist.item.InputMenuItem
import com.barista_v.debug_artist.item.PhoenixButtonMenuItem
import com.barista_v.debug_artist.item.ScalpelSwitchMenuItem
import com.barista_v.debug_artist.item.SpinnerMenuItem
import org.mockito.Mockito.mock

object MockFactory {

  fun scalpelSwitchMenuItem(checked: Boolean = false): ScalpelSwitchMenuItem =
      ScalpelSwitchMenuItem(null, checked)

  fun phoenixButtonMenuItem(): PhoenixButtonMenuItem = mock(PhoenixButtonMenuItem::class.java)

  fun spinnerMenuItem(): SpinnerMenuItem = mock(SpinnerMenuItem::class.java)

  fun inputMenuItem(): InputMenuItem = mock(InputMenuItem::class.java)

}
