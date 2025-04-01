package arc.util

import org.jetbrains.annotations.Range
import kotlin.jvm.Throws

/**
 * This interface represents some interpolatable object.
 *
 * @param T Object that can be interpolated.
 */
interface Interpolatable<T> {

    /**
     * Interpolate with other object.
     *
     * @param other Object for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return Current instance of [Interpolatable] with new values.
     *
     * @throws IllegalArgumentException If [progress] is not in ``0.0..1.0`` range.
     */
    @Throws(IllegalArgumentException::class)
    fun interpolate(other: T,  progress: @Range(from = 0, to = 1) Float): Interpolatable<T>

}