package arc.math

import kotlin.math.*

internal data class ArcVec3f(
    override val x: Float,
    override val y: Float,
    override val z: Float
) : Vec3f {

    object Factory : Vec3f.Factory {
        override fun create(x: Float, y: Float, z: Float) = ArcVec3f(x, y, z)
    }

    override fun len(): Float = sqrt(x * x + y * y + z * z)

    override fun len2(): Float = x * x + y * y + z * z

    override fun limit(limit: Float): Vec3f = limit2(limit * limit)

    override fun limit2(limit2: Float): Vec3f {
        if (len2() > limit2) {
            scl(sqrt(limit2 / len2()))
        }
        return this
    }

    override fun setLength(len: Float): Vec3f = setLength2(len * len)

    override fun setLength2(len2: Float): Vec3f {
        val oldLen2: Float = len2
        return if ((oldLen2 == 0f || oldLen2 == len2)) this else scl(sqrt(len2 / oldLen2))
    }

    override fun clamp(min: Float, max: Float): Vec3f {
        val len2: Float = len2()

        if (len2 == 0f) return this
        val max2 = max * max
        if (len2 > max2) return scl(sqrt(max2 / len2))
        val min2 = min * min
        if (len2 < min2) return scl(sqrt(min2 / len2))

        return this
    }

    override fun sub(v: Vec3f): Vec3f {
        return ArcVec3f(
            this.x - v.x,
            this.y - v.y,
            this.z - v.z
        )
    }

    override fun nor(): Vec3f {
        val len2 = this.len2()

        if (len2 == 0f || len2 == 1f) return this

        return this.scl(1f / sqrt(len2))
    }

    override fun add(v: Vec3f): Vec3f {
        return ArcVec3f(
            this.x + v.x,
            this.y + v.y,
            this.z + v.z
        )
    }

    override fun dot(v: Vec3f): Float {
        return x * v.x + y * v.y + z * v.z
    }

    override fun scl(scalar: Float): Vec3f {
        return ArcVec3f(
            this.x * scalar,
            this.y * scalar,
            this.z * scalar
        )
    }

    override fun scl(v: Vec3f): Vec3f {
        return ArcVec3f(
            this.x * v.x,
            this.y * v.y,
            this.z * v.z,
        )
    }

    override fun div(other: Vec3f): Vec3f {
        return ArcVec3f(
            this.x / other.x,
            this.y / other.y,
            this.z / other.z,
        )
    }

    override fun dst(v: Vec3f): Float {
        val a: Float = v.x - x
        val b: Float = v.y - y
        val c: Float = v.z - z

        return sqrt(a * a + b * b + c * c)
    }

    override fun dst2(v: Vec3f): Float {
        val a: Float = v.x - x
        val b: Float = v.y - y
        val c: Float = v.z - z

        return a * a + b * b + c * c
    }

    override fun isUnit(margin: Float): Boolean {
        return abs((len2() - 1f).toDouble()) < margin
    }

    override fun isZero(): Boolean = x == 0f && y == 0f && z == 0f

    override fun isZero(margin: Float): Boolean {
        return len2() < margin
    }

    override fun isOnLine(other: Vec3f, epsilon: Float): Boolean {
        return len2(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        ) <= epsilon
    }

    override fun isOnLine(other: Vec3f): Boolean {
        return len2(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        ) <= 0.000001f
    }

    override fun isCollinear(other: Vec3f, epsilon: Float): Boolean {
        return isOnLine(other, epsilon) && hasSameDirection(other)
    }

    override fun isCollinear(other: Vec3f): Boolean {
        return isOnLine(other) && hasSameDirection(other)
    }

    override fun isCollinearOpposite(other: Vec3f, epsilon: Float): Boolean {
        return isOnLine(other, epsilon) && hasOppositeDirection(other)
    }

    override fun isCollinearOpposite(other: Vec3f): Boolean {
        return isOnLine(other) && hasOppositeDirection(other)
    }

    override fun isPerpendicular(other: Vec3f): Boolean {
        return abs(dot(other)) <= 0.000001f
    }

    override fun isPerpendicular(other: Vec3f, epsilon: Float): Boolean {
        return abs(dot(other)) <= epsilon
    }

    override fun hasSameDirection(other: Vec3f): Boolean {
        return dot(other) > 0
    }

    override fun hasOppositeDirection(other: Vec3f): Boolean {
        return dot(other) < 0
    }

    override fun epsilonEquals(other: Vec3f, epsilon: Float): Boolean {
        if (abs((other.x - x)) > epsilon) return false
        if (abs((other.y - y)) > epsilon) return false

        return !(abs((other.z - z)) > epsilon)
    }

    override fun mulAdd(v: Vec3f, scalar: Float): Vec3f {
        return ArcVec3f(
            this.x + v.x * scalar,
            this.y + v.y * scalar,
            this.z + v.z * scalar
        )
    }

    override fun mulAdd(v: Vec3f, mulVec: Vec3f): Vec3f {
        return ArcVec3f(
            this.x + v.x * mulVec.x,
            this.y + v.y * mulVec.y,
            this.z + v.z * mulVec.z,
        )
    }

    override fun copy(): Vec3f {
        return ArcVec3f(x, y, z)
    }

    override fun interpolate(other: Vec3f, progress: Float): Vec3f {
        require(progress in 0.0..1.0) { "Progress value is not in 0.0..1.0 range!" }

        return ArcVec3f(
            this.x + (other.x - x) * progress,
            this.y + (other.y - y) * progress,
            this.z + (other.z - z) * progress
        )
    }

    companion object {

        /**
         * @return The squared euclidean length.
         */
        @JvmStatic
        private fun len2(x: Float, y: Float, z: Float): Float {
            return x * x + y * y + z * z
        }

    }

}