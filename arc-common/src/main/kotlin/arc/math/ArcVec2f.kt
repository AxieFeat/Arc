package arc.math

import java.util.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Suppress("LocalVariableName")
data class ArcVec2f(
    override var x: Float,
    override var y: Float
) : Vec2f {

    object Factory : Vec2f.Factory {
        override fun create(x: Float, y: Float) = ArcVec2f(x, y)
    }

    override val len: Float
        get() = sqrt(x * x + y * y)
    override val len2: Float
        get() = x * x + y * y

    override fun limit(limit: Float): Vec2f = limit2(limit * limit)

    override fun limit2(limit2: Float): Vec2f {
        val len2: Float = len2
        if (len2 > limit2) {
            return scl(sqrt(limit2 / len2))
        }
        return this
    }

    override fun setLength(len: Float): Vec2f = setLength2(len * len)

    override fun setLength2(len2: Float): Vec2f {
        val oldLen2 = len
        return if ((oldLen2 == 0f || oldLen2 == len2)) this else scl(sqrt(len2 / oldLen2))
    }

    override fun clamp(min: Float, max: Float): Vec2f {
        val len2: Float = len2

        if (len2 == 0f) return this
        val max2 = max * max
        if (len2 > max2) return scl(sqrt(max2 / len2))
        val min2 = min * min
        if (len2 < min2) return scl(sqrt(min2 / len2))

        return this
    }

    override fun set(v: Vec2f): Vec2f {
        this.x = v.x
        this.y = v.y
        return this
    }

    override fun sub(v: Vec2f): Vec2f {
        this.x -= v.x
        this.y -= v.y

        return this
    }

    override fun nor(): Vec2f {
        val len: Float = len

        if (len != 0f) {
            this.x /= len
            this.y /= len
        }

        return this
    }

    override fun add(v: Vec2f): Vec2f {
        this.x += v.x
        this.y += v.y

        return this
    }

    override fun dot(v: Vec2f): Float = x * v.x + y * v.y

    override fun scl(scalar: Float): Vec2f {
        this.x *= scalar
        this.y *= scalar

        return this
    }

    override fun scl(v: Vec2f): Vec2f {
        this.x *= v.x
        this.y *= v.y

        return this
    }

    override fun div(other: Vec2f): Vec2f {
        this.x /= other.x
        this.y /= other.y

        return this
    }

    override fun dst(v: Vec2f): Float {
        val x_d = v.x - x
        val y_d = v.y - y

        return sqrt(x_d * x_d + y_d * y_d)
    }

    override fun dst2(v: Vec2f): Float {
        val x_d = v.x - x
        val y_d = v.y - y

        return x_d * x_d + y_d * y_d
    }

    override fun lerp(target: Vec2f, alpha: Float): Vec2f {
        val invAlpha = 1.0f - alpha

        this.x = (x * invAlpha) + (target.x * alpha)
        this.y = (y * invAlpha) + (target.y * alpha)

        return this
    }

    override fun setToRandomDirection(): Vec2f {
        val theta: Float = random(0f, PI2)

        this.x = cos(theta)
        this.y = sin(theta)

        return this
    }

    override val isUnit: Boolean
        get() = isUnit(0.000000001f)

    override fun isUnit(margin: Float): Boolean {
        return abs(len2 - 1f) < margin
    }

    override val isZero: Boolean
        get() = x == 0f && y == 0f;

    override fun isZero(margin: Float): Boolean = len2 < margin

    override fun isOnLine(other: Vec2f, epsilon: Float): Boolean {
        return abs(x * other.y - y * other.x) <= epsilon
    }

    override fun isOnLine(other: Vec2f): Boolean {
        return abs(x * other.y - y * other.x) <= 0.000001f
    }

    override fun isCollinear(other: Vec2f, epsilon: Float): Boolean {
        return isOnLine(other, epsilon) && dot(other) > 0f
    }

    override fun isCollinear(other: Vec2f): Boolean {
        return isOnLine(other) && dot(other) > 0f;
    }

    override fun isCollinearOpposite(other: Vec2f, epsilon: Float): Boolean {
        return isOnLine(other, epsilon) && dot(other) < 0f
    }

    override fun isCollinearOpposite(other: Vec2f): Boolean {
        return isOnLine(other) && dot(other) < 0f
    }

    override fun isPerpendicular(other: Vec2f): Boolean {
        return abs(dot(other)) <= 0.000001f
    }

    override fun isPerpendicular(other: Vec2f, epsilon: Float): Boolean {
        return abs(dot(other)) <= epsilon
    }

    override fun hasSameDirection(other: Vec2f): Boolean {
        return dot(other) > 0
    }

    override fun hasOppositeDirection(other: Vec2f): Boolean {
        return dot(other) < 0
    }

    override fun epsilonEquals(other: Vec2f, epsilon: Float): Boolean {
        if (abs((other.x - x)) > epsilon) return false

        return !(abs((other.y - y)) > epsilon)
    }

    override fun mulAdd(v: Vec2f, scalar: Float): Vec2f {
        this.x += v.x * scalar
        this.y += v.y * scalar

        return this
    }

    override fun mulAdd(v: Vec2f, mulVec: Vec2f): Vec2f {
        this.x += v.x * mulVec.x
        this.y += v.y * mulVec.y

        return this
    }

    override fun setZero(): Vec2f {
        this.x = 0f
        this.y = 0f

        return this
    }

    override fun copy(): Vec2f {
        return ArcVec2f(x, y)
    }

    companion object {

        private const val PI2 = 3.1415927f * 2f

        private val random = Random()

        private fun random(start: Float, end: Float): Float {
            return start + random.nextFloat() * (end - start)
        }
    }

}