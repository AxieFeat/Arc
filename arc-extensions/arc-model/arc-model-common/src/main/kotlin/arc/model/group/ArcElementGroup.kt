package arc.model.group

import arc.math.Point3d
import java.util.*

internal data class ArcElementGroup(
    override val uuid: UUID,
    override val name: String,
    override val elements: Set<UUID>,
    override val origin: Point3d
) : ElementGroup {

    object Factory : ElementGroup.Factory {
        override fun create(uuid: UUID, name: String, elements: Set<UUID>, origin: Point3d): ElementGroup {
            return ArcElementGroup(uuid, name, elements, origin)
        }
    }

}