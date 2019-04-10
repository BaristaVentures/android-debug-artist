package debugartist.menu

import debugartist.menu.drawer.item.InputMenuItem
import debugartist.menu.drawer.item.PhoenixButtonMenuItem
import debugartist.menu.drawer.item.ReportBugSwitchMenuItem
import debugartist.menu.drawer.item.SpinnerMenuItem
import debugartist.menu.report_bug.Answer
import org.mockito.Mockito.mock
import io.reactivex.Observable

object MockFactory {

  fun phoenixButtonMenuItem(): PhoenixButtonMenuItem = mock(PhoenixButtonMenuItem::class.java)

  fun spinnerMenuItem(): SpinnerMenuItem = mock(SpinnerMenuItem::class.java)

  fun inputMenuItem(): InputMenuItem = mock(InputMenuItem::class.java)
  fun answer(): Observable<Answer<Any>>?
      = Observable.just(Answer())

  fun reportBugItem(checked: Boolean) = ReportBugSwitchMenuItem(checked, null)

}
