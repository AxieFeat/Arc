package arc.math

import arc.util.pattern.Copyable

/**
 * Encapsulates a general vector.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface Vector<T : Vector<T>> : Copyable<T> {

    /**
     * The euclidean length of this vector.
     */
    @get:JvmName("len")
    val len: Float

    /**
     * This method is faster than [Vector.len] because it avoids calculating a square root. It is useful for comparisons,
     * but not for getting exact lengths, as the return value is the square of the actual length.
     * @return The squared euclidean length
     */
    @get:JvmName("len2")
    val len2: Float

    /**
     * Limits the length of this vector, based on the desired maximum length.
     *
     * @param limit Desired maximum length for this vector.
     *
     * @return This vector for chaining.
     */
    fun limit(limit: Float): T

    /**
     * Limits the length of this vector, based on the desired maximum length squared.
     * This method is slightly faster than limit().
     *
     * @param limit2 Squared desired maximum length for this vector.
     *
     * @return This vector for chaining.
     *
     * @see len2
     */
    fun limit2(limit2: Float): T

    /**
     * Sets the length of this vector. Does nothing if this vector is zero.
     *
     * @param len Desired length for this vector.
     *
     * @return This vector for chaining.
     */
    fun setLength(len: Float): T

    /**
     * Sets the length of this vector, based on the square of the desired length. Does nothing if this vector is zero.
     * This method is slightly faster than setLength().
     *
     * @param len2 Desired square of the length for this vector.
     *
     * @return This vector for chaining.
     *
     * @see len2
     */
    fun setLength2(len2: Float): T

    /**
     * Clamps this vector's length to given min and max values.
     *
     * @param min Min length.
     * @param max Max length.
     *
     * @return This vector for chaining.
     */
    fun clamp(min: Float, max: Float): T

    /**
     * Sets this vector from the given vector.
     *
     * @param v The vector.
     *
     * @return This vector for chaining.
     */
    fun set(v: T): T

    /**
     * Subtracts the given vector from this vector.
     *
     * @param v The vector.
     *
     * @return This vector for chaining.
     */
    fun sub(v: T): T

    /**
     * Normalizes this vector. Does nothing if it is zero.
     *
     * @return This vector for chaining.
     */
    fun nor(): T

    /**
     * Adds the given vector to this vector.
     *
     * @param v The vector.
     *
     * @return This vector for chaining.
     */
    fun add(v: T): T

    /**
     * @param v The other vector.
     *
     * @return The dot product between this and the other vector.
     */
    fun dot(v: T): Float

    /**
     * Scales this vector by a scalar.
     *
     * @param scalar The scalar.
     *
     * @return This vector for chaining.
     */
    fun scl(scalar: Float): T

    /**
     * Scales this vector by another vector.
     *
     * @return This vector for chaining.
     */
    fun scl(v: T): T

    /**
     * Inverse of [scl].
     */
    fun div(other: T): T

    /**
     * Distance between two vectors.
     *
     * @param v The other vector.
     *
     * @return The distance between this and the other vector.
     */
    fun dst(v: T): Float

    /**
     * This method is faster than [Vector.dst] because it avoids calculating a square root. It is useful for
     * comparisons, but not for getting accurate distances, as the return value is the square of the actual distance.
     *
     * @param v The other vector.
     *
     * @return The squared distance between this and the other vector.
     */
    fun dst2(v: T): Float

    /**
     * Linearly interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is stored
     * in this vector.
     *
     * @param target The target vector.
     * @param alpha The interpolation coefficient.
     *
     * @return This vector for chaining.
     */
    fun lerp(target: T, alpha: Float): T

//    /**
//     * Interpolates between this vector and the given target vector by alpha (within range [0,1]) using the given Interpolation
//     * method. the result is stored in this vector.
//     *
//     * @param target The target vector.
//     * @param alpha The interpolation coefficient.
//     * @param interpolator An Interpolation object describing the used interpolation method.
//     *
//     * @return This vector for chaining.
//     */
//    fun interpolate(target: T, alpha: Float, interpolator: Interpolator): T

    /**
     * Sets this vector to the unit vector with a random direction.
     *
     * @return This vector for chaining.
     */
    fun setToRandomDirection(): T

    /**
     * Whether this vector is a unit length vector.
     */
    @get:JvmName("isUnit")
    val isUnit: Boolean

    /**
     * @return Whether this vector is a unit length vector within the given margin.
     */
    fun isUnit(margin: Float): Boolean

    /**
     * Whether this vector is a zero vector.
     */
    @get:JvmName("isZero")
    val isZero: Boolean

    /**
     * @return Whether the length of this vector is smaller than the given margin
     */
    fun isZero(margin: Float): Boolean

    /**
     * @return True if this vector is in line with the other vector (either in the same or the opposite direction)
     */
    fun isOnLine(other: T, epsilon: Float): Boolean

    /**
     * @return True if this vector is in line with the other vector (either in the same or the opposite direction)
     */
    fun isOnLine(other: T): Boolean

    /**
     * @return True if this vector is collinear with the other vector ([isOnLine] &&
     * [hasSameDirection]).
     */
    fun isCollinear(other: T, epsilon: Float): Boolean

    /**
     * @return True if this vector is collinear with the other vector ([isOnLine] &&
     * [hasSameDirection]).
     */
    fun isCollinear(other: T): Boolean

    /**
     * @return True if this vector is opposite collinear with the other vector ([isOnLine] &&
     * [hasOppositeDirection]).
     */
    fun isCollinearOpposite(other: T, epsilon: Float): Boolean

    /**
     * @return True if this vector is opposite collinear with the other vector ([isOnLine] &&
     * [hasOppositeDirection]).
     */
    fun isCollinearOpposite(other: T): Boolean

    /**
     * @return Whether this vector is perpendicular with the other vector. True if the dot product is 0.
     */
    fun isPerpendicular(other: T): Boolean

    /**
     * @param epsilon A positive small number close to zero.
     *
     * @return Whether this vector is perpendicular with the other vector. True if the dot product is 0.
     */
    fun isPerpendicular(other: T, epsilon: Float): Boolean

    /**
     * @return Whether this vector has similar direction compared to the other vector. True if the normalized dot product is > 0.
     */
    fun hasSameDirection(other: T): Boolean

    /**
     * @return Whether this vector has opposite direction compared to the other vector. True if the normalized dot product is < 0.
     */
    fun hasOppositeDirection(other: T): Boolean

    /**
     * Compares this vector with the other vector, using the supplied epsilon for fuzzy equality testing.
     *
     * @return Whether the vectors have fuzzy equality.
     */
    fun epsilonEquals(other: T, epsilon: Float): Boolean

    /**
     * First scale a supplied vector, then add it to this vector.
     *
     * @param v addition vector.
     *
     * @param scalar for scaling the addition vector.
     */
    fun mulAdd(v: T, scalar: Float): T

    /**
     * First scale a supplied vector, then add it to this vector.
     *
     * @param v addition vector.
     *
     * @param mulVec vector by whose values the addition vector will be scaled.
     */
    fun mulAdd(v: T, mulVec: T): T

    /**
     * Sets the components of this vector to 0.
     *
     * @return This vector for chaining.
     */
    fun setZero(): T

    /**
     * Create copy of this vector.
     *
     * @return A copy of this vector.
     */
    override fun copy(): T
}