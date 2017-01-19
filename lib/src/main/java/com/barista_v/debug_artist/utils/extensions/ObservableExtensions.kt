package com.barista_v.debug_artist.utils.extensions

import com.barista_v.debug_artist.utils.RetryAfterTimeoutWithDelay
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Shorthand to set [subscribeOn], [observeOn] and retry policy for observables
 */
fun <T> Observable<T>.composeForIoTasks(): Observable<T> = compose<T>(Observable.Transformer {
  it.subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .retryWhen(RetryAfterTimeoutWithDelay(3, 500))
})