package debugartist.menu

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import debugartist.menu.drawer.DebugDrawerPresenter
import debugartist.menu.drawer.DebugDrawerView
import debugartist.menu.drawer.item.input.InputItemListener
import debugartist.menu.utils.device.AndroidDevice
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertNull

class DebugDrawerPresenterTests {
  val view: DebugDrawerView = mock()
  val device: AndroidDevice = mock()

  lateinit var presenter: DebugDrawerPresenter

  @Before
  fun setUp() {
    Mockito.reset(view,device)

    presenter = DebugDrawerPresenter()
    presenter.attach(view, device)
  }

  @Test
  fun test_onDettach_releaseResources() {
    presenter.deAttach()

    assertNull(presenter.view)
    assertNull(presenter.inputItemListener)
  }

  @Test
  fun test_onSpinnerAdded_add() {
    val item = MockFactory.spinnerMenuItem()
    presenter.onItemAdded(item)
    verify(view).addSpinnerItem(item)
  }

  @Test
  fun test_onInputAdded_add() {
    val item = MockFactory.inputMenuItem()
    presenter.onItemAdded(item)
    verify(view).addInputItem(item)
  }

  @Test
  fun test_onInput_callListenerWithSameValues() {
    val inputListener = mock<InputItemListener>()

    presenter.inputItemListener = inputListener
    presenter.onTextInputEntered(1, "asd")

    verify(inputListener).onTextInputEnter(1, "asd")
  }

}
