package com.barista_v.debug_artist.utils.extensions

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * Shorthand to set [subscribeOn] and [observeOn] thread for observables,
 * show/hide progress dialogs when needed and retry requests on timeouts.
 */
fun <T> Observable<T>.composeForIoTasks(): Observable<T> = compose<T>(Observable.Transformer {
  it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
})