package debug_artist.menu.utils


import rx.Observable
import rx.functions.Func1
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
class RetryAfterTimeoutWithDelay(val maxRetries: Int, var delay: Long, val delayAmount: Long = 100)
  : Func1<Observable<out Throwable>, Observable<*>> {

  internal var retryCount = 0

  override fun call(attempts: Observable<out Throwable>): Observable<*> {
    return attempts.flatMap({
      if (++retryCount < maxRetries && it is SocketTimeoutException) {
        delay += delayAmount
        Observable.timer(delay, TimeUnit.MILLISECONDS)
      } else {
        Observable.error(it as Throwable)
      }
    })
  }
}