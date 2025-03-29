package arc.lwamodel.animation

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
internal data class ArcLWAModelAnimation(
    @Contextual
    override val uuid: UUID,
    override val isLoop: Boolean,
    override val length: Long
) : LWAModelAnimation