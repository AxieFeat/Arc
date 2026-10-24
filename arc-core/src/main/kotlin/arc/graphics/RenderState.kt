package arc.graphics

/** Operations implemented by a render-context instance. A missing operation throws, rather than drawing incorrectly. */
data class RenderCapabilities(
    val scopedPass: Boolean = false,
    val scissor: Boolean = false,
    val typedState: Boolean = false,
    val renderTargets: Boolean = false,
    val stencilMasks: Boolean = false,
    val maxTextureDimension: Int? = null,
)

/** Rectangle in render-target pixels, with a lower-left origin. Empty rectangles reject every fragment. */
data class PixelRect(val x: Int, val y: Int, val width: Int, val height: Int) {

    init {
        require(width >= 0 && height >= 0) { "Pixel rectangle dimensions must be nonnegative" }
    }
}

enum class BlendFactor {
    ZERO, ONE, SOURCE_COLOR, ONE_MINUS_SOURCE_COLOR, DESTINATION_COLOR, ONE_MINUS_DESTINATION_COLOR,
    SOURCE_ALPHA, ONE_MINUS_SOURCE_ALPHA, DESTINATION_ALPHA, ONE_MINUS_DESTINATION_ALPHA,
}

enum class BlendEquation {
    ADD,
    SUBTRACT,
    REVERSE_SUBTRACT,
    MIN,
    MAX
}

data class BlendState(
    val enabled: Boolean = true,
    val sourceColor: BlendFactor = BlendFactor.ONE,
    val destinationColor: BlendFactor = BlendFactor.ONE_MINUS_SOURCE_ALPHA,
    val sourceAlpha: BlendFactor = BlendFactor.ONE,
    val destinationAlpha: BlendFactor = BlendFactor.ONE_MINUS_SOURCE_ALPHA,
    val colorEquation: BlendEquation = BlendEquation.ADD,
    val alphaEquation: BlendEquation = BlendEquation.ADD,
) {

    companion object {

        val PREMULTIPLIED_ALPHA = BlendState()
        val STRAIGHT_ALPHA = BlendState(sourceColor = BlendFactor.SOURCE_ALPHA)
        val DISABLED = BlendState(enabled = false)
    }
}

enum class CompareFunction {
    NEVER,
    LESS,
    EQUAL,
    LESS_OR_EQUAL,
    GREATER,
    NOT_EQUAL,
    GREATER_OR_EQUAL,
    ALWAYS
}

data class DepthState(
    val testEnabled: Boolean = false,
    val writeEnabled: Boolean = false,
    val comparison: CompareFunction = CompareFunction.LESS,
)

enum class CullMode {
    NONE,
    FRONT,
    BACK,
    FRONT_AND_BACK
}

enum class PolygonMode {
    FILL,
    LINE,
    POINT
}

/** Default raster state permits ordinary filled UI/overlay triangles regardless of application raster state. */
data class RasterState(
    val polygonMode: PolygonMode = PolygonMode.FILL,
    val discard: Boolean = false,
    val logicOperation: Boolean = false,
    val alphaToCoverage: Boolean = false,
    val sampleCoverage: Boolean = false,
    val sampleMask: Boolean = false,
    val clipDistances: Set<Int> = emptySet(),
) {

    init {
        require(clipDistances.all { it >= 0 }) { "Clip-distance indices must be nonnegative" }
    }
}

data class ColorWriteMask(
    val red: Boolean = true,
    val green: Boolean = true,
    val blue: Boolean = true,
    val alpha: Boolean = true,
) {

    companion object {

        val ALL = ColorWriteMask()
        val NONE = ColorWriteMask(false, false, false, false)
    }
}

enum class StencilOperation {
    KEEP,
    ZERO,
    REPLACE,
    INCREMENT_CLAMP,
    DECREMENT_CLAMP,
    INVERT,
    INCREMENT_WRAP,
    DECREMENT_WRAP
}

/**
 * Stencil state for both faces. Mask geometry uses ordinary draw calls while color/depth writes are disabled.
 * Callers must own the stencil attachment/bits; this API never clears an application's stencil implicitly.
 */
data class StencilState(
    val enabled: Boolean = false,
    val comparison: CompareFunction = CompareFunction.ALWAYS,
    val reference: Int = 0,
    val compareMask: Int = -1,
    val writeMask: Int = -1,
    val stencilFail: StencilOperation = StencilOperation.KEEP,
    val depthFail: StencilOperation = StencilOperation.KEEP,
    val pass: StencilOperation = StencilOperation.KEEP,
)
