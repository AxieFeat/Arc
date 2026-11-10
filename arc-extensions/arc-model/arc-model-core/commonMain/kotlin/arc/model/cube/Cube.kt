package arc.model.cube

import kotlin.jvm.JvmStatic
import arc.Arc.factory
import arc.math.AABB
import arc.model.Face
import arc.util.Color
import arc.util.pattern.Copyable
import arc.util.pattern.Identifiable
import arc.annotations.InternalApi
import korlibs.math.geom.Vector3F
import kotlin.uuid.Uuid

/**
 * Represents a cube element in a 3D model.
 *
 * @see Cube
 * @see CubeFace
 * @see Face
 */
interface Cube : Copyable<Cube>, Identifiable {

    /**
     * Represents the starting point of the cube in 3D space.
     */
    val from: Vector3F

    /**
     * Represents the endpoint of the cube in 3D space.
     */
    val to: Vector3F

    /**
     * Bounding box of this cube, based on [from], [to] and [rotation] values.
     */
    val aabb: AABB

    /**
     * The pivot point for rotation cube.
     */
    val pivot: Vector3F

    /**
     * Rotation of this cube.
     */
    val rotation: Vector3F

    /**
     * The light emission level of a cube.
     */
    val lightLevel: Byte

    /**
     * The light color of this cube in model.
     */
    val lightColor: Color

    /**
     * All faces of this cube, that will be rendered.
     */
    val faces: Map<Face, CubeFace>

    /**
     * Translate position of this cube by offsets.
     *
     * @param offsetX X offset.
     * @param offsetY Y offset.
     * @param offsetZ Z offset.
     */
    fun translate(offsetX: Float, offsetY: Float, offsetZ: Float)

    /**
     * Remove faces by filter.
     *
     * @param filter Filter for removing faces.
     */
    fun removeFaceIf(filter: (Map.Entry<Face, CubeFace>) -> Boolean)

    @Suppress("TooManyFunctions")
    @InternalApi
    interface Builder : arc.util.pattern.Builder<Cube> {

        fun setUUID(uuid: Uuid): Builder

        fun setFrom(from: Vector3F): Builder
        fun setFrom(x: Float, y: Float, z: Float): Builder = setFrom(Vector3F(x, y, z))

        fun setTo(to: Vector3F): Builder
        fun setTo(x: Float, y: Float, z: Float): Builder = setTo(Vector3F(x, y, z))

        fun setPivot(pivot: Vector3F): Builder
        fun setPivot(x: Float, y: Float, z: Float): Builder = setPivot(Vector3F(x, y, z))

        fun setRotation(rotation: Vector3F): Builder
        fun setRotation(x: Float, y: Float, z: Float): Builder = setRotation(Vector3F(x, y, z))

        fun setLightLevel(lightLevel: Byte): Builder
        fun setLightColor(lightColor: Color): Builder

        fun addFace(face: Face, cubeFace: CubeFace): Builder
    }

    companion object {

        /**
         * Create new instance of [Cube] via builder.
         *
         * @return New instance of [Builder].
         */
        @JvmStatic
        fun builder(): Builder {
            return factory<Builder>()
        }
    }
}
