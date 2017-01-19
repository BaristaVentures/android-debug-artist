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
    val bugRepositoryBuilder = mock<BugRepository.Builder>()
    val bugRepository = mock<BugRepository>()
    val extrasHandler = mock<ExtrasHandler>()
    var presenter = ReportBugPresenter()

    beforeEachTest {
      mockSchedulers()

      whenever(bugRepositoryBuilder.build()).thenReturn(bugRepository)
      whenever(extrasHandler.extraRepositoryBuilder).thenReturn(bugRepositoryBuilder)
      whenever(extrasHandler.screenshotFilePath).thenReturn("any.jpg")

      presenter = ReportBugPresenter().apply { attach(view, extrasHandler) }

      Mockito.reset(view, bugRepositoryBuilder, bugRepository, extrasHandler)
    }

    on("attach") {
      whenever(bugRepositoryBuilder.build()).thenReturn(bugRepository)
      whenever(extrasHandler.extraRepositoryBuilder).thenReturn(bugRepositoryBuilder)
      whenever(extrasHandler.screenshotFilePath).thenReturn("any.jpg")

      presenter.attach(view, extrasHandler)

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
    }
  }

})