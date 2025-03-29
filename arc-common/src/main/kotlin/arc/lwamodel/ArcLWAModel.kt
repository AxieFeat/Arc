package arc.lwamodel

import kotlinx.serialization.Serializable
import arc.model.animation.Animation
import arc.model.texture.ModelTexture
import arc.util.UUIDSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.protobuf.ProtoBuf
import java.util.*

@Serializable
internal data class ArcLWAModel(
    override val elements: List<LWAModelElement> = listOf(),
    override val textures: List<ModelTexture> = listOf(),
    override val animations: List<Animation> = listOf()
) : LWAModel {

    @OptIn(ExperimentalSerializationApi::class)
    object Factory : LWAModel.Factory {

        private val proto = ProtoBuf(ProtoBuf.Default) {
            serializersModule = SerializersModule {
                contextual(UUID::class, UUIDSerializer)
            }
        }

        override fun create(bytes: ByteArray): LWAModel {
            return proto.decodeFromByteArray<ArcLWAModel>(bytes)
        }

    }

}