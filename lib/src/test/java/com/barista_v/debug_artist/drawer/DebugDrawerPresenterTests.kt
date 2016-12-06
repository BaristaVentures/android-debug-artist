package com.barista_v.debug_artist.drawer

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

  describe("a new presenter") {
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

    on("leak cananry item selected") {
      presenter.onLeakCanaryItemSelected()

      it("should enable leak canary") {
        verify(actor).enableLeakCanary()
      }
    }

    on("leak canary item selected") {
      presenter.onLeakCanarySwitchAdded(true)

      it("should enable leak canary") {
        verify(actor).enableLeakCanary()
      }
    }

    on("leak canary switch uncheck") {
      presenter.onLeakCanarySwitchAdded(false)

      it("do nothing") {
        verifyZeroInteractions(actor)
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