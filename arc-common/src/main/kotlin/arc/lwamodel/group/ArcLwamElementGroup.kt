package arc.lwamodel.group

import arc.math.Point3d
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ArcLwamElementGroup(
    @Contextual
    override val uuid: UUID,
    override val name: String,
    override val elements: Set<@Contextual UUID>,
    override val origin: Point3d
) : LwamElementGroup {

    object Factory : LwamElementGroup.Factory {
        override fun create(uuid: UUID, name: String, elements: Set<UUID>, origin: Point3d): LwamElementGroup {
            return ArcLwamElementGroup(uuid, name, elements, origin)
        }
    }

}