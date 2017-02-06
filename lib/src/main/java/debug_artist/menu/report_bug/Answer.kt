package debug_artist.menu.report_bug


import retrofit2.Response
import retrofit2.adapter.rxjava.HttpException

/**
 * Proxy to read, parse and organize backend responses and errors
 *
 * @property body null if no there was a weird error (some kind of unexpected exception)
 * @property error null if no error
 */
open class Answer<out T>(val body: T? = null, val error: ApiError? = null) {

  companion object {

    /**
     * Initialize Answer from backend answers parsing errors with defined format.
     *
     * TODO: move to other place
     */
    fun <T> from(response: Response<T>): Answer<T> =
        Answer(response.body(), ApiError.build(response))

    /**
     * Initialize Answer from retrofit error
     *
     * TODO: move to other place
     *
     * HttpException -> non 200
     * IOException -> network error
     * Other -> Unknown
     */
    fun <T> from(throwable: Throwable): Answer<T> {
      val apiError = when (throwable) {
        is HttpException -> ApiError.Companion.build(throwable.response(), throwable)
        else -> ApiError(throwable)
      }
      return Answer(error = apiError)
    }

  }

}

open class ApiError(val cause: Throwable) {

  companion object {

    internal fun <T> build(response: Response<T>, cause: Throwable? = null): ApiError? {
      if (response.isSuccessful) {
        return null
      } else {
        val code = response.code()
        val errorBody = response.errorBody().string()
        val realCause = cause ?: Throwable("($code) $errorBody")

        return ApiError(realCause)
      }
    }
  }

}