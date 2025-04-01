package arc.util

import arc.OS

/**
 * This class represents timer for ticking.
 *
 * @param ticksPerSecond The number of timer ticks per second of real time
 */
internal class Timer(
    val ticksPerSecond: Float
) {
    /**
     * The time reported by the high-resolution clock at the last call of updateTimer(), in seconds
     */
    private var lastHRTime = 0.0

    /**
     * How many full ticks have turned over since the last call to updateTimer(), capped at 10.
     */
    var elapsedTicks: Int = 0

    /**
     * How much time has elapsed since the last tick, in ticks, for use by display rendering routines (range: 0.0 -
     * 1.0).  This field is frozen if the display is paused to eliminate jitter.
     */
    var renderPartialTicks: Float = 0f

    /**
     * A multiplier to make the timer (and therefore the game) go faster or slower.  0.5 makes the game run at half-
     * speed.
     */
    var timerSpeed: Float = 1.0f

    /**
     * How much time has elapsed since the last tick, in ticks (range: 0.0 - 1.0).
     */
    var elapsedPartialTicks: Float = 0f

    /**
     * The time reported by the system clock at the last sync, in milliseconds
     */
    private var lastSyncSysClock: Long

    /**
     * The time reported by the high-resolution clock at the last sync, in milliseconds
     */
    private var lastSyncHRClock: Long

    /** Increase per 1 every tick, reset when reach 1000  */
    private var counter: Long = 0

    /**
     * A ratio used to sync the high-resolution clock to the system clock, updated once per second
     */
    private var timeSyncAdjustment = 1.0

    init {
        this.lastSyncSysClock = OS.getTime()
        this.lastSyncHRClock = System.nanoTime() / 1000000L
    }

    /**
     * Updates all fields of the Timer using the current time
     */
    fun update() {
        val i: Long = OS.getTime()
        val j = i - this.lastSyncSysClock
        val k = System.nanoTime() / 1000000L
        val d0 = k.toDouble() / 1000.0

        if (j in 0L..1000L) {
            this.counter += j

            if (this.counter > 1000L) {
                val l = k - this.lastSyncHRClock
                val d1 = counter.toDouble() / l.toDouble()
                this.timeSyncAdjustment += (d1 - this.timeSyncAdjustment) * 0.20000000298023224
                this.lastSyncHRClock = k
                this.counter = 0L
            }

            if (this.counter < 0L) {
                this.lastSyncHRClock = k
            }
        } else {
            this.lastHRTime = d0
        }

        this.lastSyncSysClock = i
        var d2 = (d0 - this.lastHRTime) * this.timeSyncAdjustment
        this.lastHRTime = d0
        d2 = clampDouble(d2, 0.0, 1.0)
        this.elapsedPartialTicks =
            (elapsedPartialTicks.toDouble() + d2 * timerSpeed.toDouble() * ticksPerSecond.toDouble()).toFloat()
        this.elapsedTicks = elapsedPartialTicks.toInt()
        this.elapsedPartialTicks -= elapsedTicks.toFloat()

        if (this.elapsedTicks > 10) {
            this.elapsedTicks = 10
        }

        this.renderPartialTicks = this.elapsedPartialTicks
    }

    companion object {
        private fun clampDouble(num: Double, min: Double, max: Double): Double {
            return if (num < min) min else (if (num > max) max else num)
        }
    }
}
