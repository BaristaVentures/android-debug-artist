package com.barista_v.debug_artist.report_bug

import com.barista_v.debug_artist.MockFactory.answer
import com.barista_v.debug_artist.mockSchedulers
import com.barista_v.debug_artist.repositories.BugReportRepository
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito
import org.mockito.Mockito.*
import rx.Observable.error

class ReportBugPresenterTests : Spek({

  describe("a new ReportBug presenter") {
    val view = mock(ReportBugView::class.java)
    val bugReportRepository = mock(BugReportRepository::class.java)
    val extrasHandler = mock(ExtrasHandler::class.java)
    var presenter = ReportBugPresenter()

    beforeEachTest {
      mockSchedulers()

      Mockito.reset(view, bugReportRepository, extrasHandler)
      presenter = ReportBugPresenter().apply { attach(view, extrasHandler) }
    }

    on("attach") {
      it("should build repository") {
        verify(extrasHandler).extraRepositoryBuilder
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
      `when`(bugReportRepository.createBug("title", "description")).thenReturn(answer())
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
      `when`(bugReportRepository.createBug("title", "description")).thenReturn(error(error))

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