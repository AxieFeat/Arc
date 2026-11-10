package arc.profiler.section

import kotlin.time.TimeSource

internal object ProfilerClock {
    private val origin = TimeSource.Monotonic.markNow()

    fun nanoTime(): Long = origin.elapsedNow().inWholeNanoseconds
}
