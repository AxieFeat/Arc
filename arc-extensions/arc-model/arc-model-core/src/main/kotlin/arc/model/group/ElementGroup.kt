package arc.model.group

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.math.Point3d
import arc.model.animation.Animator
import arc.util.pattern.Identifiable
import org.jetbrains.annotations.ApiStatus
import java.util.UUID

/**
 * This interface represents group of elements.
 */
@ImmutableType
interface ElementGroup : Identifiable {

    /**
     * Name of group. Used by [Animator.target].
     */
    val name: String

    /**
     * All elements UUID's of this group.
     */
    val elements: Set<UUID>

    /**
     * Origin point of this group.
     */
    val origin: Point3d

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(uuid: UUID, name: String, elements: Set<UUID>, origin: Point3d): ElementGroup

    }

    companion object {

        /**
         * Create instance of [ElementGroup].
         *
         * @param uuid Unique id of group.
         * @param name Name of group.
         * @param elements Elements of group.
         * @param origin Center point of group.
         *
         * @return New instance of [ElementGroup].
         */
        @JvmStatic
        fun of(
            uuid: UUID = UUID.randomUUID(),
            name: String = "",
            elements: Set<UUID> = setOf(),
            origin: Point3d = Point3d.ZERO
        ): ElementGroup {
            return Arc.factory<Factory>().create(uuid, name, elements, origin)
        }

    }

}