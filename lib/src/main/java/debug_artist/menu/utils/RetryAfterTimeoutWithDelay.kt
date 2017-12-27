package debug_artist.menu.utils


import io.reactivex.functions.Function
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * Retry observable subscription if timeout.
 *
 * For every retry it will wait delay + delayAmount so we wait more and more every retry.
 *
 * @param maxRetries number of retries
 * @param delay milliseconds of wait between each try
 * @param delayAmount  delay + delayAmount
 */
class RetryAfterTimeoutWithDelay(private val maxRetries: Int,
                                 private var delay: Long,
                                 private val delayAmount: Long = 100)
  : Function<io.reactivex.Observable<Throwable>, io.reactivex.Observable<*>> {

  private var retryCount = 0

  override fun apply(attempts: io.reactivex.Observable<Throwable>): io.reactivex.Observable<*> {
    return attempts.flatMap {
      if (++retryCount < maxRetries && it is SocketTimeoutException) {
        delay += delayAmount
        io.reactivex.Observable.timer(delay, TimeUnit.MILLISECONDS)
      } else {
        io.reactivex.Observable.error(it)
      }
    }
  }

}