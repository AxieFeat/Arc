package arc.util.pattern

import org.jetbrains.annotations.Range
import kotlin.jvm.Throws

/**
 * This interface represents some interpolatable object.
 *
 * @param T Object that can be interpolated.
 */
interface Interpolatable<T> {

    /**
     * Interpolate with another object.
     *
     * @param other Object for interpolation.
     * @param progress Progress of interpolation in ``0.0..1.0`` range.
     *
     * @return New instance of [Interpolatable] with new values.
     *
     * @throws IllegalArgumentException If [progress] is not in ``0.0..1.0`` range.
     */
    @Throws(IllegalArgumentException::class)
    fun interpolate(other: T,  progress: @Range(from = 0, to = 1) Float): Interpolatable<T>

    companion object {

        /**
         * Minimum value for [Interpolatable.interpolate] function.
         */
        const val MIN_INTERPOLATE_VALUE = 0.0f

        /**
         * Maximum value for [Interpolatable.interpolate] function.
         */
        const val MAX_INTERPOLATE_VALUE = 1.0f
    }
}
