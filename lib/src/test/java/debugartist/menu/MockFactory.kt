package debugartist.menu

import debugartist.menu.drawer.item.InputMenuItem
import debugartist.menu.drawer.item.SpinnerMenuItem
import org.mockito.Mockito.mock

object MockFactory {
    fun spinnerMenuItem(): SpinnerMenuItem = mock(SpinnerMenuItem::class.java)
    fun inputMenuItem(): InputMenuItem = mock(InputMenuItem::class.java)
}
