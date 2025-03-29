package arc.lwamodel

import arc.lwamodel.animation.LWAModelAnimation
import arc.lwamodel.texture.LWAModelTexture
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
internal data class ArcLWAModel(
    override val elements: List<LWAModelElement> = listOf(),
    override val textures: List<LWAModelTexture> = listOf(),
    override val animations: List<LWAModelAnimation> = listOf()
) : LWAModel {

    override fun serialize(): ByteArray {
        return proto.encodeToByteArray(this)
    }

    object Factory : LWAModel.Factory {

        private val proto = ProtoBuf(ProtoBuf.Default) {
            serializersModule = SerializersModule {
                contextual(UUID::class, UUIDSerializer)
            }
        }

        override fun create(bytes: ByteArray): LWAModel {
            return proto.decodeFromByteArray<ArcLWAModel>(bytes)
        }

        override fun create(
            elements: List<LWAModelElement>,
            textures: List<LWAModelTexture>,
            animations: List<LWAModelAnimation>
        ): LWAModel {
            return ArcLWAModel(elements, textures, animations)
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