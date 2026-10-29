package arc.graphics

/**
 * Operations implemented by a render-context instance. A missing operation throws, rather than drawing incorrectly.
 *
 * @property scopedPass Supports scoped capture and restoration of application state.
 * @property scissor Supports rectangular clipping in target pixels.
 * @property typedState Supports explicit blend, depth, culling and raster state.
 * @property renderTargets Supports owned offscreen color attachments.
 * @property stencilMasks Supports exact clipping through stencil operations.
 * @property maxTextureDimension Maximum texture side in pixels, or null when unknown.
 */
data class RenderCapabilities(
    val scopedPass: Boolean = false,
    val scissor: Boolean = false,
    val typedState: Boolean = false,
    val renderTargets: Boolean = false,
    val stencilMasks: Boolean = false,
    val maxTextureDimension: Int? = null,
)

/**
 * Rectangle in render-target pixels, with a lower-left origin. Empty rectangles reject every fragment.
 *
 * @property x Left edge in pixels.
 * @property y Bottom edge in pixels.
 * @property width Nonnegative horizontal extent.
 * @property height Nonnegative vertical extent.
 */
data class PixelRect(val x: Int, val y: Int, val width: Int, val height: Int) {

    init {
        require(width >= 0 && height >= 0) { "Pixel rectangle dimensions must be nonnegative" }
    }
}

/**
 * Factors applied to source or destination color during blending.
 */
enum class BlendFactor {

    /**
     * Multiplies by zero.
     */
    ZERO,

    /**
     * Keeps the value unchanged.
     */
    ONE,

    /**
     * Multiplies by source color.
     */
    SOURCE_COLOR,

    /**
     * Multiplies by the complement of source color.
     */
    ONE_MINUS_SOURCE_COLOR,

    /**
     * Multiplies by destination color.
     */
    DESTINATION_COLOR,

    /**
     * Multiplies by the complement of destination color.
     */
    ONE_MINUS_DESTINATION_COLOR,

    /**
     * Multiplies by source alpha.
     */
    SOURCE_ALPHA,

    /**
     * Multiplies by the complement of source alpha.
     */
    ONE_MINUS_SOURCE_ALPHA,

    /**
     * Multiplies by destination alpha.
     */
    DESTINATION_ALPHA,

    /**
     * Multiplies by the complement of destination alpha.
     */
    ONE_MINUS_DESTINATION_ALPHA
}

/**
 * Combines factored source and destination values.
 */
enum class BlendEquation {

    /**
     * Adds source and destination.
     */
    ADD,

    /**
     * Subtracts destination from source.
     */
    SUBTRACT,

    /**
     * Subtracts source from destination.
     */
    REVERSE_SUBTRACT,

    /**
     * Selects the smaller value.
     */
    MIN,

    /**
     * Selects the larger value.
     */
    MAX
}

/**
 * Independent RGB and alpha blending configuration.
 *
 * @property enabled Enables blending rather than replacing the destination.
 * @property sourceColor Source RGB factor.
 * @property destinationColor Destination RGB factor.
 * @property sourceAlpha Source alpha factor.
 * @property destinationAlpha Destination alpha factor.
 * @property colorEquation RGB combination rule.
 * @property alphaEquation Alpha combination rule.
 */
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

        /**
         * Source-over blending for colors already multiplied by alpha.
         */
        val PREMULTIPLIED_ALPHA = BlendState()

        /**
         * Source-over blending for straight-alpha colors.
         */
        val STRAIGHT_ALPHA = BlendState(sourceColor = BlendFactor.SOURCE_ALPHA)

        /**
         * Replaces destination values without blending.
         */
        val DISABLED = BlendState(enabled = false)
    }
}

/**
 * Comparison between the incoming value and the stored depth or stencil value.
 */
enum class CompareFunction {

    /**
     * Always fails.
     */
    NEVER,

    /**
     * Passes when the incoming value is smaller.
     */
    LESS,

    /**
     * Passes when values match.
     */
    EQUAL,

    /**
     * Passes when the incoming value is no larger.
     */
    LESS_OR_EQUAL,

    /**
     * Passes when the incoming value is larger.
     */
    GREATER,

    /**
     * Passes when values differ.
     */
    NOT_EQUAL,

    /**
     * Passes when the incoming value is no smaller.
     */
    GREATER_OR_EQUAL,

    /**
     * Always passes.
     */
    ALWAYS
}

/**
 * Controls depth testing and depth attachment writes independently.
 *
 * @property testEnabled Enables the depth comparison.
 * @property writeEnabled Allows depth writes for passing fragments.
 * @property comparison Comparison used when testing is enabled.
 */
data class DepthState(
    val testEnabled: Boolean = false,
    val writeEnabled: Boolean = false,
    val comparison: CompareFunction = CompareFunction.LESS,
)

/**
 * Faces discarded before fragment processing.
 */
enum class CullMode {

    /**
     * Keeps both faces.
     */
    NONE,

    /**
     * Discards front-facing polygons.
     */
    FRONT,

    /**
     * Discards back-facing polygons.
     */
    BACK,

    /**
     * Discards all polygons.
     */
    FRONT_AND_BACK
}

/**
 * Polygon rasterization mode.
 */
enum class PolygonMode {

    /**
     * Rasterizes filled polygons.
     */
    FILL,

    /**
     * Rasterizes polygon edges.
     */
    LINE,

    /**
     * Rasterizes polygon vertices.
     */
    POINT
}

/**
 * Default raster state permits ordinary filled UI/overlay triangles regardless of application raster state.
 *
 * @property polygonMode Controls polygon filling.
 * @property discard Disables rasterization when true.
 * @property logicOperation Enables the current color logic operation.
 * @property alphaToCoverage Enables multisample alpha coverage.
 * @property sampleCoverage Enables the current sample coverage value.
 * @property sampleMask Enables the current sample mask.
 * @property clipDistances Enabled shader clip-distance indices.
 */
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

/**
 * Selects color attachment channels that may be written.
 *
 * @property red Enables red writes.
 * @property green Enables green writes.
 * @property blue Enables blue writes.
 * @property alpha Enables alpha writes.
 */
data class ColorWriteMask(
    val red: Boolean = true,
    val green: Boolean = true,
    val blue: Boolean = true,
    val alpha: Boolean = true,
) {

    companion object {

        /**
         * Allows every color channel to be written.
         */
        val ALL = ColorWriteMask()

        /**
         * Preserves every color channel, including during stencil-only draws.
         */
        val NONE = ColorWriteMask(false, false, false, false)
    }
}

/**
 * Updates a stencil value after testing a fragment.
 */
enum class StencilOperation {

    /**
     * Preserves the stored value.
     */
    KEEP,

    /**
     * Writes zero.
     */
    ZERO,

    /**
     * Writes the stencil reference.
     */
    REPLACE,

    /**
     * Increments, stopping at the maximum value.
     */
    INCREMENT_CLAMP,

    /**
     * Decrements, stopping at zero.
     */
    DECREMENT_CLAMP,

    /**
     * Inverts every stencil bit.
     */
    INVERT,

    /**
     * Increments with wraparound.
     */
    INCREMENT_WRAP,

    /**
     * Decrements with wraparound.
     */
    DECREMENT_WRAP
}

/**
 * Stencil state for both faces. Mask geometry uses ordinary draw calls while color/depth writes are disabled.
 * Callers must own the stencil attachment/bits; this API never clears an application's stencil implicitly.
 *
 * @property enabled Enables stencil testing.
 * @property comparison Compares masked reference and stored stencil values.
 * @property reference Reference used for comparison and replacement.
 * @property compareMask Selects bits participating in comparison.
 * @property writeMask Selects bits that operations may modify.
 * @property stencilFail Operation when stencil testing fails.
 * @property depthFail Operation when stencil passes but depth fails.
 * @property pass Operation when both tests pass.
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
