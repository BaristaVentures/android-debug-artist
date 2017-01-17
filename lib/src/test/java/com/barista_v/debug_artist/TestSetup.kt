package com.barista_v.debug_artist

import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaPlugins
import rx.plugins.RxJavaSchedulersHook
import rx.schedulers.Schedulers

fun mockSchedulers() {
  resetSchedulersMock()

  RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
    override fun getMainThreadScheduler() = Schedulers.immediate()
  })

  RxJavaPlugins.getInstance().registerSchedulersHook(object : RxJavaSchedulersHook() {
    override fun getIOScheduler() = Schedulers.immediate()
    override fun getComputationScheduler() = Schedulers.immediate()
    override fun getNewThreadScheduler() = Schedulers.immediate()

  })
}

fun resetSchedulersMock() {
  RxJavaPlugins.getInstance().reset()
  RxAndroidPlugins.getInstance().reset()
}