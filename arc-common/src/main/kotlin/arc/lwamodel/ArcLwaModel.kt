package arc.lwamodel

import arc.lwamodel.animation.LwamAnimation
import arc.lwamodel.group.LwamElementGroup
import arc.lwamodel.texture.LwamTexture
import arc.util.UUIDSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.protobuf.ProtoBuf
import java.util.*

@Serializable
internal data class ArcLwaModel(
    override val elements: List<LwamElement> = listOf(),
    override val groups: List<LwamElementGroup> = listOf(),
    override val animations: List<LwamAnimation> = listOf(),
    override val textures: List<LwamTexture> = listOf(),
) : LwaModel {

    object Factory : LwaModel.Factory {
        override fun create(
            elements: List<LwamElement>,
            groups: List<LwamElementGroup>,
            animations: List<LwamAnimation>,
            textures: List<LwamTexture>,
        ): LwaModel {
            return ArcLwaModel(elements, groups, animations, textures)
        }
    }

}