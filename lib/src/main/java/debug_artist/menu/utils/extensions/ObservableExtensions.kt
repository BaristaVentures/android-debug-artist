package debug_artist.menu.utils.extensions

import debug_artist.menu.utils.RetryAfterTimeoutWithDelay
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Shorthand to set [subscribeOn], [observeOn] and retry policy for observables
 */
fun <T> Observable<T>.composeForIoTasks() = compose<T> {
  it.subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .retryWhen(RetryAfterTimeoutWithDelay(10, 1000))
}