package com.barista_v.debug_artist.drawer

import com.barista_v.debug_artist.item.LeakCanarySwitchMenuItem
import com.barista_v.debug_artist.item.LynksButtonMenuItem
import com.barista_v.debug_artist.item.PicassoLogsSwitchMenuItem
import com.barista_v.debug_artist.item.StethoSwitchMenuItem
import com.barista_v.debug_artist.item.input.InputItemListener
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import kotlin.test.assertNull

@RunWith(JUnitPlatform::class)
class DebugDrawerPresenterTests : Spek({

  describe("a new DebugDrawer presenter") {
    var view = Mockito.mock(DebugDrawerView::class.java)
    var actor = Mockito.mock(Actor::class.java)
    var presenter = DebugDrawerPresenter().apply { onAttach(view, actor) }

    beforeEachTest {
      view = Mockito.mock(DebugDrawerView::class.java)
      actor = Mockito.mock(Actor::class.java)
      presenter = DebugDrawerPresenter().apply { onAttach(view, actor) }
    }

    on("onDetach") {
      presenter.onDetach()

      it("should release resources onDetach") {
        assertNull(presenter.view)
        assertNull(presenter.actor)
        assertNull(presenter.inputItemListener)
        assertNull(presenter.restartListener)
      }
    }

    on("add switch stetho checked") {
      presenter.onItemAdded(StethoSwitchMenuItem(checked = true))

      it("should enable") {
        verify(actor).enableStetho()
        verify(view).addStethoSwitch(true)
      }
    }

    on("add switch stetho unchecked") {
      presenter.onItemAdded(StethoSwitchMenuItem())

      it("should not enable") {
        verifyZeroInteractions(actor)
        verify(view).addStethoSwitch(false)
      }
    }

    on("add switch Leak Canary checked") {
      presenter.onItemAdded(LeakCanarySwitchMenuItem(checked = true))

      it("should enable it") {
        verify(actor).enableLeakCanary()
        verify(view).addLeakCanarySwitch(true)
      }
    }

    on("add switch Leak Canary unchecked") {
      presenter.onItemAdded(LeakCanarySwitchMenuItem())

      it("should not enable") {
        verifyZeroInteractions(actor)
        verify(view).addLeakCanarySwitch(false)
      }
    }

    on("add switch Picasso Logs checked") {
      presenter.onItemAdded(PicassoLogsSwitchMenuItem(checked = true))

      it("should enable it") {
        verify(actor).enablePicassoLogs()
        verify(view).addPicassoLogsSwitch(true)
      }
    }

    on("add switch Picasso Logs unchecked") {
      presenter.onItemAdded(PicassoLogsSwitchMenuItem())

      it("should enable it") {
        verifyZeroInteractions(actor)
        verify(view).addPicassoLogsSwitch(false)
      }
    }

    on("add switch Scalpel Layout checked") {
      presenter.onItemAdded(MockFactory.scalpelSwitchMenuItem(checked = true))

      it("should enable it") {
        verify(actor).enableScalpelLayout()
        verify(view).addScalpelSwitch(true)
      }
    }

    on("add switch Scalpel Layout unchecked") {
      presenter.onItemAdded(MockFactory.scalpelSwitchMenuItem())

      it("should enable it") {
        verifyZeroInteractions(actor)
        verify(view).addScalpelSwitch(false)
      }
    }

    on("add button Lynks") {
      presenter.onItemAdded(LynksButtonMenuItem())

      it("should add") {
        verify(view).addLynksButton()
      }
    }

    on("add button Phoenix") {
      presenter.onItemAdded(MockFactory.phoenixButtonMenuItem())

      it("should add") {
        verify(view).addPhoenixButton()
      }
    }

    on("add button Spinner") {
      val item = MockFactory.spinnerMenuItem()
      presenter.onItemAdded(item)

      it("should add") {
        verify(view).addSpinnerItem(item)
      }
    }

    on("add button Input") {
      val item = MockFactory.inputMenuItem()
      presenter.onItemAdded(item)

      it("should add") {
        verify(view).addInputItem(item)
      }
    }

    on("lynks item selected") {
      presenter.onLynksItemSelected()

      it("should enable lynks") {
        verify(actor).enableLynx()
      }
    }

    on("phoenix item selected") {
      presenter.onPhoenixItemSelected()

      it("should trigger app rebirth") {
        verify(actor).triggerAppRebirth()
      }
    }

    on("picasso item selected") {
      presenter.onPicassoItemSelected()

      it("should enable picasso logs") {
        verify(actor).enablePicassoLogs()
      }
    }

    on("scalpel item checked") {
      presenter.onScalpelItemSelected(true)

      it("should enable scalpel") {
        verify(actor).enableScalpelLayout()
      }
    }

    on("scalpel item unchecked") {
      presenter.onScalpelItemSelected(false)

      it("should disable scalpel") {
        verify(actor).disableScalpelLayout()
      }
    }

    on("stetho item checked") {
      presenter.onStethoItemSelected()

      it("should enable stetho") {
        verify(actor).enableStetho()
      }
    }

    describe("new text listener") {
      var inputListener = mock(InputItemListener::class.java)

      beforeEachTest {
        inputListener = mock(InputItemListener::class.java)
        presenter.inputItemListener = inputListener
      }

      on("text input entered") {
        presenter.onTextInputEntered(1, "asd")

        it("call listener with same values") {
          verify(inputListener).onTextInputEnter(1, "asd")
        }
      }

    }
  }

})