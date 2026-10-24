package arc.font

import arc.util.pattern.Cleanable

enum class FontDirection {
    LTR,
    RTL
}

/** Typographic metrics in the same logical units as the requested em size. */
data class FontMetrics(val ascent: Float, val descent: Float, val lineGap: Float)

/** Cluster offsets are UTF-16 code units, with an exclusive clusterEnd. */
data class ShapedGlyph(
    val glyphId: Int,
    val clusterStart: Int,
    val clusterEnd: Int,
    val advanceX: Float,
    val advanceY: Float,
    val offsetX: Float,
    val offsetY: Float
)

class ShapedRun(
    val text: String,
    val direction: FontDirection,
    val size: Float,
    val metrics: FontMetrics,
    glyphs: List<ShapedGlyph>
) {

    val glyphs: List<ShapedGlyph> = glyphs.toList()
}

/** Row-major, eight-bit coverage. Pixel bearings locate the bitmap relative to its baseline. */
class RasterGlyph(val width: Int, val height: Int, val left: Int, val top: Int, alpha: ByteArray) {

    private val coverage = alpha.copyOf()

    init {
        require(width >= 0 && height >= 0 && width.toLong() * height == alpha.size.toLong())
    }

    val alpha: ByteArray get() = coverage.copyOf()
}

/**
 * A font face is provider-owned. Shape and raster use the same glyph IDs and source font data.
 * A provider documents its thread ownership; no render context or GPU objects are required.
 */
interface FontFace {

    val id: String
    val version: Long
    fun contains(codepoint: Int): Boolean
    fun metrics(size: Float): FontMetrics
    fun shape(text: String, size: Float, direction: FontDirection = FontDirection.LTR, language: String = "und"): ShapedRun
    fun rasterize(glyphId: Int, size: Float, pixelsPerUnit: Float = 1f): RasterGlyph
}

/** Explicitly created service. cleanup releases all loaded faces; it is idempotent. */
interface FontProvider : Cleanable {

    /** Change [version] when replacing the bytes under a stable [id]; live duplicate IDs are rejected. */
    fun load(id: String, data: ByteArray, faceIndex: Int = 0, version: Long = 0): FontFace
}
