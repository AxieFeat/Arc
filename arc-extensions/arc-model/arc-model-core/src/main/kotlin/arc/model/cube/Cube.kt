package arc.model.cube

import arc.Arc
import arc.math.AABB
import arc.math.Vec3f
import arc.model.Face
import arc.util.Color
import arc.util.pattern.Copyable
import arc.util.pattern.Identifiable
import org.jetbrains.annotations.ApiStatus
import java.util.*
import java.util.function.Predicate

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
    val from: Vec3f

    /**
     * Represents the endpoint of the cube in 3D space.
     */
    val to: Vec3f

    /**
     * Bounding box of this cube, based on [from], [to] and [rotation] values.
     */
    val aabb: AABB

    /**
     * The pivot point for rotation cube.
     */
    val pivot: Vec3f

    /**
     * Rotation of this cube.
     */
    val rotation: Vec3f

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
    fun removeFaceIf(filter: Predicate<Map.Entry<Face, CubeFace>>)

    @ApiStatus.Internal
    interface Builder : arc.util.pattern.Builder<Cube> {

        fun setUUID(uuid: UUID): Builder

        fun setFrom(from: Vec3f): Builder
        fun setFrom(x: Float, y: Float, z: Float): Builder = setFrom(Vec3f.of(x, y, z))

        fun setTo(to: Vec3f): Builder
        fun setTo(x: Float, y: Float, z: Float): Builder = setTo(Vec3f.of(x, y, z))

        fun setPivot(pivot: Vec3f): Builder
        fun setPivot(x: Float, y: Float, z: Float): Builder = setPivot(Vec3f.of(x, y, z))

        fun setRotation(rotation: Vec3f): Builder
        fun setRotation(x: Float, y: Float, z: Float): Builder = setRotation(Vec3f.of(x, y, z))

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
            return Arc.factory<Builder>()
        }

    }

}