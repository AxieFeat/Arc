package arc.model.group

import kotlin.jvm.JvmStatic
import arc.Arc.factory
import arc.annotations.ImmutableType
import arc.model.animation.Animator
import arc.model.cube.Cube
import arc.util.pattern.Copyable
import arc.annotations.InternalApi
import korlibs.math.geom.Vector3F
import kotlin.uuid.Uuid

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
     * All Cubes Uuid's of this group.
     */
    val cubes: Set<Uuid>

    /**
     * Pivot point for rotation.
     */
    val pivot: Vector3F

    @Suppress("SpreadOperator")
    @InternalApi
    interface Builder : arc.util.pattern.Builder<ElementGroup> {

        fun setName(name: String): Builder

        fun addCube(vararg cube: Cube): Builder = addCube(*cube.map { it.uuid }.toTypedArray())
        fun addCube(vararg cube: Uuid): Builder

        fun setPivot(pivot: Vector3F): Builder
        fun setPivot(x: Float, y: Float, z: Float): Builder = setPivot(Vector3F(x, y, z))
    }

    companion object {

        /**
         * Create instance of [ElementGroup] via builder.
         *
         * @return New instance of [Builder].
         */
        @JvmStatic
        fun builder(): Builder {
            return factory<Builder>()
        }
    }
}
