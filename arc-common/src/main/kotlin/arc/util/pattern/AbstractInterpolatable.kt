package arc.util.pattern

import arc.util.pattern.Interpolatable.Companion.MAX_INTERPOLATE_VALUE
import arc.util.pattern.Interpolatable.Companion.MIN_INTERPOLATE_VALUE
import org.jetbrains.annotations.Range

/**
 * This abstract class exist for check progress range and throwing exception.
 */
abstract class AbstractInterpolatable<T : Interpolatable<T>> : Interpolatable<T> {

    final override fun interpolate(other: T, progress: @Range(from = 0, to = 1) Float): T {
        require(progress in MIN_INTERPOLATE_VALUE..MAX_INTERPOLATE_VALUE) { "Progress value is not in $INTERPOLATION_PRETTY_RANGE range!" }

        return doInterpolation(other, progress)
    }

    protected abstract fun doInterpolation(other: T, progress: @Range(from = 0, to = 1) Float): T

    companion object {

        private const val INTERPOLATION_PRETTY_RANGE = "$MIN_INTERPOLATE_VALUE..$MAX_INTERPOLATE_VALUE"
    }
}
