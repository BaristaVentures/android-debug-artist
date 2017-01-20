package com.barista_v.debug_artist.report_bug

import com.barista_v.debug_artist.MockFactory.answer
import com.barista_v.debug_artist.mockSchedulers
import com.barista_v.debug_artist.repositories.BugRepository
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito
import rx.Observable.error

@RunWith(JUnitPlatform::class)
class ReportBugPresenterTests : Spek({

  describe("a new ReportBug presenter") {
    val view = mock<ReportBugView>()
    val bugRepositoryBuilder = mock<BugRepository.Builder>()
    val bugRepository = mock<BugRepository>()
    val extrasHandler = mock<ExtrasHandler>()
    var presenter = ReportBugPresenter()
    val traveler = mock<ReportBugTraveler>()

    beforeEachTest {
      mockSchedulers()

      whenever(bugRepositoryBuilder.build()).thenReturn(bugRepository)
      whenever(extrasHandler.extraRepositoryBuilder).thenReturn(bugRepositoryBuilder)
      whenever(extrasHandler.screenshotFilePath).thenReturn("any.jpg")

      presenter = ReportBugPresenter().apply { attach(view, traveler, extrasHandler) }

      Mockito.reset(view, bugRepositoryBuilder, bugRepository, extrasHandler, traveler)
    }

    on("attach") {
      whenever(bugRepositoryBuilder.build()).thenReturn(bugRepository)
      whenever(extrasHandler.extraRepositoryBuilder).thenReturn(bugRepositoryBuilder)
      whenever(extrasHandler.screenshotFilePath).thenReturn("any.jpg")

      presenter.attach(view, traveler, extrasHandler)

      it("should build repository") {
        verify(bugRepositoryBuilder).build()
      }

      it("should set screenshot image") {
        verify(view).setScreenshotImage("any.jpg")
      }
    }

    on("sendButton with success") {
      whenever(extrasHandler.screenshotFilePath).thenReturn("any.jpg")
      whenever(extrasHandler.logsFilePath).thenReturn("log.log")
      whenever(bugRepository.create("title", "description", "any.jpg", "log.log"))
          .thenReturn(answer())

      presenter.onSendButtonClick("title", "description")

      it("should create bug") {
        verify(view).apply {
          showProgressDialog()
          dismissProgressDialog()
          showSuccessToast()
        }
      }

      it("should close") {
        verify(traveler).close()
      }
    }

    on("sendButton with empty title") {
      presenter.onSendButtonClick("", "description")

      it("should show error") {
        verify(view).apply {
          showErrorDialog(any())
        }

        verifyNoMoreInteractions(view)
      }
    }

    on("sendButton with empty description") {
      presenter.onSendButtonClick("title", "")

      it("should show error") {
        verify(view).apply {
          showErrorDialog(any())
        }

        verifyNoMoreInteractions(view)
      }
    }

    on("sendButton with error") {
      val error = Throwable("Something happened")

      whenever(extrasHandler.screenshotFilePath).thenReturn("any.jpg")
      whenever(extrasHandler.logsFilePath).thenReturn("log.log")
      whenever(bugRepository.create("title", "description", "any.jpg", "log.log"))
          .thenReturn(error(error))

      presenter.onSendButtonClick("title", "description")

      it("should show dialog with error") {
        verify(view).apply {
          showProgressDialog()
          dismissProgressDialog()
          showErrorDialog(error.message!!)
        }
      }

      it("should not close") {
        verifyZeroInteractions(traveler)
      }
    }
  }

})