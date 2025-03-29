package arc.lwamodel.texture

import arc.model.texture.ModelTexture
import kotlinx.serialization.Serializable

@Serializable
internal data class ArcLWAModelTexture(
    override val id: Int = 0,
    override val width: Int = 0,
    override val height: Int = 0,
    override val rows: Int = 0,
    override val columns: Int = 0,
    override val base64Image: String = ""
) : ModelTexture