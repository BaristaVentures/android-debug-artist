package debugartist.menu

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

fun mockSchedulers() {
  resetSchedulersMock()

  RxJavaPlugins.setSingleSchedulerHandler { Schedulers.trampoline() }
  RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
  RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
  RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }

  RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
}

fun resetSchedulersMock() {
  RxJavaPlugins.reset()
  RxAndroidPlugins.reset()
}