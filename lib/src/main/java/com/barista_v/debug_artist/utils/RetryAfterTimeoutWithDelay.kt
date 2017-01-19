package com.barista_v.debug_artist.utils


import rx.Observable
import rx.functions.Func1
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * Retry observable subscription if timeout.
 *
 * @param maxRetries number of retries
 * @param initialDelay milliseconds of wait between each try
 */
class RetryAfterTimeoutWithDelay(val maxRetries: Int, var initialDelay: Long, val delay: Long = 100)
  : Func1<Observable<out Throwable>, Observable<*>> {

  internal var retryCount = 0

  override fun call(attempts: Observable<out Throwable>): Observable<*> {
    return attempts.flatMap({
      if (++retryCount < maxRetries && it is SocketTimeoutException) {
        initialDelay += delay
        Observable.timer(initialDelay, TimeUnit.MILLISECONDS)
      } else {
        Observable.error(it as Throwable)
      }
    })
  }
}