package com.barista_v.debug_artist.item.issue_reporter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


open class ShakeDetector(val context: Context) : SensorEventListener {
  var listener: OnShakeListener? = null
  private var shakeTimestamp: Long = 0
  private var shakeCount: Int = 0
  private var sensorManager: SensorManager? = null

  fun start() {
    sensorManager = (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager).apply {
      val accelerometerSensor = getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
      registerListener(this@ShakeDetector, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
    }
  }

  fun pause() {
    sensorManager?.unregisterListener(this)
  }

  override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    // ignore
  }

  override fun onSensorChanged(event: SensorEvent) {
    if (listener == null) return

    val x = event.values[0]
    val y = event.values[1]
    val z = event.values[2]

    val gX = x / SensorManager.GRAVITY_EARTH
    val gY = y / SensorManager.GRAVITY_EARTH
    val gZ = z / SensorManager.GRAVITY_EARTH

    // gForce will be close to 1 when there is no movement.
    val gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble())

    if (gForce > SHAKE_THRESHOLD_GRAVITY) {
      val now = System.currentTimeMillis()
      // ignore shake events too close to each other (500ms)
      if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
        return
      }

      // reset the shake count after 3 seconds of no shakes
      if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
        shakeCount = 0
      }

      shakeTimestamp = now
      shakeCount++

      listener?.onShake(shakeCount)
    }
  }

  companion object {
    /*
     * The gForce that is necessary to register as shake.
     * Must be greater than 1G (one earth gravity unit).
     * You can install "G-Force", by Blake La Pierre
     * from the Google Play Store and run it to see how
     *  many G's it takes to register a shake
     */
    private val SHAKE_THRESHOLD_GRAVITY = 2.7f
    private val SHAKE_SLOP_TIME_MS = 500
    private val SHAKE_COUNT_RESET_TIME_MS = 3000
  }

  interface OnShakeListener {
    fun onShake(count: Int)
  }
}