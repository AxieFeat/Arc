package arc.lwamodel.cube

import arc.lwamodel.texture.ArcLWAModelTexture
import arc.math.Point2i
import arc.model.cube.CubeFace
import arc.model.texture.ModelTexture
import kotlinx.serialization.Serializable

@Serializable
internal data class ArcLWAModelCubeFace(
    override val uv: Point2i = Point2i.ZERO,
    override val texture: ModelTexture = ArcLWAModelTexture(),
) : CubeFace