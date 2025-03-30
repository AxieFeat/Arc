package arc.lwamodel.group

import arc.Arc
import arc.annotations.TypeFactory
import arc.math.Point3d
import arc.model.group.ElementGroup
import org.jetbrains.annotations.ApiStatus
import java.util.UUID

/**
 * This interface represents group of elements in LWAM format.
 */
interface LwamElementGroup : ElementGroup {

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(uuid: UUID, name: String, elements: Set<UUID>, origin: Point3d): LwamElementGroup

    }

    companion object {

        /**
         * Create instance of [LwamElementGroup].
         *
         * @param uuid Unique id of group.
         * @param name Name of group.
         * @param elements Elements of group.
         * @param origin Center point of group.
         *
         * @return New instance of [LwamElementGroup].
         */
        @JvmStatic
        fun of(
            uuid: UUID = UUID.randomUUID(),
            name: String = "",
            elements: Set<UUID> = setOf(),
            origin: Point3d = Point3d.ZERO
        ): LwamElementGroup {
            return Arc.factory<Factory>().create(uuid, name, elements, origin)
        }

    }

}