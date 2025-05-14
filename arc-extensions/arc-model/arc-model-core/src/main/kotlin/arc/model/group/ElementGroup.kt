package arc.model.group

import arc.Arc
import arc.annotations.ImmutableType
import arc.math.Point3d
import arc.math.Vec3f
import arc.model.animation.Animation
import arc.model.animation.Animator
import arc.model.cube.Cube
import arc.util.pattern.Copyable
import arc.util.pattern.Identifiable
import org.jetbrains.annotations.ApiStatus
import java.util.UUID

/**
 * This interface represents group of elements.
 */
@ImmutableType
interface ElementGroup : Copyable<ElementGroup> {

    /**
     * Name of group. Used by [Animator.target].
     */
    val name: String

    /**
     * All Cubes UUID's of this group.
     */
    val cubes: Set<UUID>

    /**
     * Pivot point for rotation.
     */
    val pivot: Vec3f

    @ApiStatus.Internal
    interface Builder : arc.util.pattern.Builder<ElementGroup> {

        fun setName(name: String): Builder

        fun addCube(vararg cube: Cube): Builder = addCube(*cube.map { it.uuid }.toTypedArray())
        fun addCube(vararg cube: UUID): Builder

        fun setPivot(pivot: Vec3f): Builder
        fun setPivot(x: Float, y: Float, z: Float): Builder = setPivot(Vec3f.of(x, y, z))

    }

    companion object {

        /**
         * Create instance of [ElementGroup] via builder.
         *
         * @return New instance of [Builder].
         */
        @JvmStatic
        fun builder(): Builder {
            return Arc.factory<Builder>()
        }

    }

}