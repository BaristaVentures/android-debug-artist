package debug_artist.menu

import com.jraska.falcon.Falcon
import com.nhaarman.mockito_kotlin.*
import debug_artist.menu.drawer.Actor
import debug_artist.menu.drawer.DebugDrawerPresenter
import debug_artist.menu.drawer.DebugDrawerView
import debug_artist.menu.drawer.Traveler
import debug_artist.menu.drawer.item.LeakCanarySwitchMenuItem
import debug_artist.menu.drawer.item.LynksButtonMenuItem
import debug_artist.menu.drawer.item.PicassoLogsSwitchMenuItem
import debug_artist.menu.drawer.item.StethoSwitchMenuItem
import debug_artist.menu.drawer.item.input.InputItemListener
import debug_artist.menu.report_bug.BugRepository
import debug_artist.menu.utils.device.AndroidDevice
import debug_artist.menu.utils.shake_detector.ShakeDetector
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import kotlin.test.assertNull

class DebugDrawerPresenterTests {
  val view: DebugDrawerView = mock()
  val actor: Actor = mock()
  val shakeDetector: ShakeDetector = mock()
  val traveler: Traveler = mock()
  val device: AndroidDevice = mock()

  lateinit var presenter: DebugDrawerPresenter

  @Before
  fun setUp() {
    mockSchedulers()

    Mockito.reset(view, traveler, actor, shakeDetector, device)

    presenter = DebugDrawerPresenter()
    presenter.attach(view, traveler, actor, shakeDetector, device)
  }

  @Test
  fun test_onDettach_releaseResources() {
    presenter.deAttach()

    assertNull(presenter.view)
    assertNull(presenter.actor)
    assertNull(presenter.inputItemListener)
    assertNull(presenter.restartListener)
    verify(shakeDetector).pause()
  }

  @Test
  fun test_onStethoAdded_ifChecked_enable() {
    presenter.onItemAdded(StethoSwitchMenuItem(checked = true))

    verify(actor).enableStetho()
    verify(view).addStethoSwitch(true)
  }

  @Test
  fun test_onStethoAdded_ifUnchecked_dontEnable() {
    presenter.onItemAdded(StethoSwitchMenuItem())

    verifyZeroInteractions(actor)
    verify(view).addStethoSwitch(false)
  }

  @Test
  fun test_onLeakAdded_ifChecked_enable() {
    presenter.onItemAdded(LeakCanarySwitchMenuItem(checked = true))

    verify(actor).enableLeakCanary()
    verify(view).addLeakCanarySwitch(true)
  }

  @Test
  fun test_onLeakAdded_ifNotChecked_dontEnable() {
    presenter.onItemAdded(LeakCanarySwitchMenuItem())

    verifyZeroInteractions(actor)
    verify(view).addLeakCanarySwitch(false)
  }

  @Test
  fun test_onPicassoAdded_ifChecked_enable() {
    presenter.onItemAdded(PicassoLogsSwitchMenuItem(checked = true))

    verify(actor).enablePicassoLogs()
    verify(view).addPicassoLogsSwitch(true)
  }

  @Test
  fun test_onPicassoAdded_ifNotChecked_dontEnable() {
    presenter.onItemAdded(PicassoLogsSwitchMenuItem())

    verifyZeroInteractions(actor)
    verify(view).addPicassoLogsSwitch(false)
  }

  @Test
  fun test_onLynksAdded_add() {
    presenter.onItemAdded(LynksButtonMenuItem())
    verify(view).addLynksButton()
  }

  @Test
  fun test_onPhoenixAdded_add() {
    presenter.onItemAdded(MockFactory.phoenixButtonMenuItem())
    verify(view).addPhoenixButton()
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
  fun test_reportBugAdded_enable_andStartShakeDetector() {
    val item = MockFactory.reportBugItem(true)

    presenter.onItemAdded(item)

    verify(view).addBugReportSwitch(true)
    verify(shakeDetector).start(presenter)
  }

  @Test
  fun test_onLynksSelected_enable() {
    presenter.onLynksItemSelected()
    verify(actor).enableLynx()
  }

  @Test
  fun test_onPhoenixSelected_trigger() {
    presenter.onPhoenixItemSelected()
    verify(actor).triggerAppRebirth()
  }

  @Test
  fun test_onPicassoSelected_enable() {
    presenter.onPicassoItemSelected()
    verify(actor).enablePicassoLogs()
  }

  @Test
  fun test_onStethoSelected_enable() {
    presenter.onStethoItemSelected()
    verify(actor).enableStetho()
  }

  @Test
  fun test_onReportBugSelected_ifChecked_startShakeListener() {
    presenter.onBugReporterItemSelected(true)
    verify(shakeDetector).start(presenter)
  }

  @Test
  fun test_bugReporterSelected_ifNotChecked_pauseShakeListener() {
    presenter.onBugReporterItemSelected(false)
    verify(shakeDetector).pause()
  }

  @Test
  fun test_onShake_with2_doNothing() {
    presenter.onShake(2)
    verifyNoMoreInteractions(view, actor, traveler, shakeDetector, device)
  }

  @Test
  fun test_onShake_with1_startReportBug() {
    val repositoryBuilder = mock<BugRepository.Builder>()

    whenever(device.takeScreenshot(anyString())).thenReturn("any.jpg")
    whenever(device.readLogFile()).thenReturn("log.log")

    presenter.bugRepositoryBuilder = repositoryBuilder
    presenter.onShake(1)

    verify(traveler).startBugReportView(repositoryBuilder, "any.jpg", "log.log")
  }

  @Test
  fun test_onShake_with1_withNullScreenshot_doNothing() {
    whenever(device.takeScreenshot(anyString())).thenReturn(null)
    whenever(device.readLogFile()).thenReturn("log.log")

    presenter.bugRepositoryBuilder = mock<BugRepository.Builder>()
    presenter.onShake(1)

    verifyZeroInteractions(view, actor, shakeDetector, traveler)
  }

  @Test
  fun test_onShake_with1_withNullLogFile_doNothing() {
    whenever(device.takeScreenshot(anyString())).thenReturn("any.jpg")
    whenever(device.readLogFile()).thenReturn(null)

    presenter.bugRepositoryBuilder = mock<BugRepository.Builder>()
    presenter.onShake(1)

    verifyZeroInteractions(view, actor, shakeDetector, traveler)
  }

  @Test
  fun test_onShake_with1_withExceptionTakingScreenshot_showError() {
    val error = mock<Falcon.UnableToTakeScreenshotException>()
    whenever(device.takeScreenshot(anyString())).thenThrow(error)
    whenever(device.readLogFile()).thenReturn("log.log")

    presenter.onShake(1)

    verify(view).showErrorDialog(anyString())
  }

  @Test
  fun test_onInput_callListenerWithSameValues() {
    val inputListener = mock<InputItemListener>()

    presenter.inputItemListener = inputListener
    presenter.onTextInputEntered(1, "asd")

    verify(inputListener).onTextInputEnter(1, "asd")
  }

}
