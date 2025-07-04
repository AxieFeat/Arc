package arc.model.group

import arc.Arc
import arc.annotations.ImmutableType
import arc.model.animation.Animator
import arc.model.cube.Cube
import arc.util.pattern.Copyable
import org.jetbrains.annotations.ApiStatus
import org.joml.Vector3f
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
    val pivot: Vector3f

    @ApiStatus.Internal
    interface Builder : arc.util.pattern.Builder<ElementGroup> {

        fun setName(name: String): Builder

        fun addCube(vararg cube: Cube): Builder = addCube(*cube.map { it.uuid }.toTypedArray())
        fun addCube(vararg cube: UUID): Builder

        fun setPivot(pivot: Vector3f): Builder
        fun setPivot(x: Float, y: Float, z: Float): Builder = setPivot(Vector3f(x, y, z))

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