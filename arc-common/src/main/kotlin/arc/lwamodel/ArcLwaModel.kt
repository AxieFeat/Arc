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
@OptIn(ExperimentalSerializationApi::class)
internal data class ArcLwaModel(
    override val elements: List<LwamElement> = listOf(),
    override val groups: List<LwamElementGroup> = listOf(),
    override val animations: List<LwamAnimation> = listOf(),
    override val textures: List<LwamTexture> = listOf(),
) : LwaModel {

    override fun serialize(): ByteArray {
        return proto.encodeToByteArray(this)
    }

    object Factory : LwaModel.Factory {

        private val proto = ProtoBuf(ProtoBuf.Default) {
            serializersModule = SerializersModule {
                contextual(UUID::class, UUIDSerializer)
            }
        }

        override fun create(bytes: ByteArray): LwaModel {
            return proto.decodeFromByteArray<ArcLwaModel>(bytes)
        }

        override fun create(
            elements: List<LwamElement>,
            groups: List<LwamElementGroup>,
            animations: List<LwamAnimation>,
            textures: List<LwamTexture>,
        ): LwaModel {
            return ArcLwaModel(elements, groups, animations, textures)
        }

    }

    companion object {
        private val proto = ProtoBuf(ProtoBuf.Default) {
            serializersModule = SerializersModule {
                contextual(UUID::class, UUIDSerializer)
            }
        }
    }

}