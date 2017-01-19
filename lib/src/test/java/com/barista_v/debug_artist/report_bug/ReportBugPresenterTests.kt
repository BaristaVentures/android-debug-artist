package com.barista_v.debug_artist.report_bug

import com.barista_v.debug_artist.MockFactory.answer
import com.barista_v.debug_artist.mockSchedulers
import com.barista_v.debug_artist.repositories.BugRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito
import rx.Observable.error

//@RunWith(JUnitPlatform::class)
class ReportBugPresenterTests : Spek({

  describe("a new ReportBug presenter") {
    val view = mock<ReportBugView>()
    val bugReportRepository = mock<BugRepository>()
    val extrasHandler = mock<ExtrasHandler>()
    var presenter = ReportBugPresenter()

    beforeEachTest {
      mockSchedulers()

      Mockito.reset(view, bugReportRepository, extrasHandler)

      whenever(extrasHandler.extraRepositoryBuilder.build()).thenReturn(bugReportRepository)

      presenter = ReportBugPresenter().apply { attach(view, extrasHandler) }
    }

    on("attach") {
      whenever(extrasHandler.screenshotFilePath).thenReturn("any.jpg")

      it("should build repository") {
        verify(extrasHandler).extraRepositoryBuilder
      }

      it("should set screenshot image") {
        verify(view).setScreenshotImage("any.jpg")
      }
    }

    on("sendButton") {
      presenter.onSendButtonClick("title", "description")

      it("should create bug and close") {
        verify(view).apply {
          showProgressDialog()
          dismissProgressDialog()
          showSuccessToast()
        }
      }
    }

    on("sendButton with success") {
      whenever(extrasHandler.screenshotFilePath).thenReturn("http://any")
      whenever(extrasHandler.logsFilePath).thenReturn("http://any")
      whenever(bugReportRepository.createBug("title", "description", "http://any", "http://any"))
          .thenReturn(answer())

      presenter.onSendButtonClick("title", "description")

      it("should create bug and close") {
        verify(view).apply {
          showProgressDialog()
          dismissProgressDialog()
          showSuccessToast()
        }
      }
    }

    on("sendButton with error") {
      val error = Throwable("Something happened")
      whenever(bugReportRepository.createBug("title", "description", "http://any", "http://any"))
          .thenReturn(error(error))

      presenter.onSendButtonClick("title", "description")

      it("should show dialog with error") {
        verify(view).apply {
          showProgressDialog()
          dismissProgressDialog()
          showErrorDialog(error.message!!)
        }
      }
    }
  }

})