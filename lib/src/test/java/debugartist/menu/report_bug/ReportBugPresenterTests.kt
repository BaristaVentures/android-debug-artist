package debugartist.menu.report_bug

import com.nhaarman.mockito_kotlin.*
import debugartist.menu.MockFactory.answer
import debugartist.menu.mockSchedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import io.reactivex.Observable

class ReportBugPresenterTests {
  val view: ReportBugView = mock()
  val bugRepositoryBuilder: BugRepository.Builder = mock()
  val bugRepository: BugRepository = mock()
  val extrasHandler: ExtrasHandler = mock()
  val traveler: ReportBugTraveler = mock()

  var presenter = ReportBugPresenter()

  @Before
  fun setUp() {
    mockSchedulers()

    whenever(bugRepositoryBuilder.build()).thenReturn(bugRepository)
    whenever(extrasHandler.extraRepositoryBuilder).thenReturn(bugRepositoryBuilder)
    whenever(extrasHandler.screenshotFilePath).thenReturn("any.jpg")

    presenter = ReportBugPresenter().apply { attach(view, traveler, extrasHandler) }

    Mockito.reset(view, bugRepositoryBuilder, bugRepository, extrasHandler, traveler)
  }

  @Test
  fun test_onAttach_buildRepository_andSetScreenshotImage() {
    whenever(bugRepositoryBuilder.build()).thenReturn(bugRepository)
    whenever(extrasHandler.extraRepositoryBuilder).thenReturn(bugRepositoryBuilder)
    whenever(extrasHandler.screenshotFilePath).thenReturn("any.jpg")

    presenter.attach(view, traveler, extrasHandler)

    verify(bugRepositoryBuilder).build()
    verify(view).setScreenshotImage("any.jpg")
  }

  @Test
  fun test_onSendButtonClick_createBug() {
    whenever(extrasHandler.screenshotFilePath).thenReturn("any.jpg")
    whenever(extrasHandler.logsFilePath).thenReturn("log.log")
    whenever(bugRepository.create("title", "description", "any.jpg", "log.log"))
        .thenReturn(answer())

    presenter.onSendButtonClick("title", "description")

    verify(view).showProgressDialog()
    verify(view).dismissProgressDialog()
    verify(view).showSuccessToast()
    verify(traveler).close()
  }

  @Test
  fun test_onSendButtonClick_withEmptyTitle_showError() {
    presenter.onSendButtonClick("", "description")

    verify(view).showErrorDialog(any())
    verifyNoMoreInteractions(view)
  }

  @Test
  fun test_onSendButtonClick_withEmptyDescription_showError() {
    presenter.onSendButtonClick("title", "")

    verify(view).showErrorDialog(any())
    verifyNoMoreInteractions(view)
  }

  @Test
  fun test_onSendButtonClick_withErrorOnRequest_showError() {
    val error = Throwable("Something happened")

    whenever(extrasHandler.screenshotFilePath).thenReturn("any.jpg")
    whenever(extrasHandler.logsFilePath).thenReturn("log.log")
    whenever(bugRepository.create("title", "description", "any.jpg", "log.log"))
        .thenReturn(Observable.error(error))

    presenter.onSendButtonClick("title", "description")


    verify(view).showProgressDialog()
    verify(view).dismissProgressDialog()
    verify(view).showErrorDialog(error.message!!)
    verifyZeroInteractions(traveler)
  }

}