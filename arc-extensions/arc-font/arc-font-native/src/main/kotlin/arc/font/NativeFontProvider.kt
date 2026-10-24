package arc.font

import org.lwjgl.system.Configuration
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.util.freetype.FT_Face
import org.lwjgl.util.freetype.FreeType
import org.lwjgl.util.harfbuzz.HarfBuzz
import java.nio.ByteBuffer
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToLong

/** Explicit native font factory. The resulting provider belongs to the creating thread. */
object NativeFontProvider {

    fun of(): FontProvider = FreeTypeFontProvider()
}

private class FreeTypeFontProvider : FontProvider {

    private val thread = Thread.currentThread()
    private val library: Long
    private val faces = LinkedHashMap<String, NativeFace>()
    private var closed = false

    init {
        // LWJGL FreeType embeds HarfBuzz with hb_ft_*; standalone HarfBuzz does not.
        Configuration.HARFBUZZ_LIBRARY_NAME.set(FreeType.getLibrary())
        library = MemoryStack.stackPush().use { stack ->
            val result = stack.mallocPointer(1)
            checkFreeType(FreeType.FT_Init_FreeType(result), "initialize FreeType")
            result[0]
        }
    }

    private fun checkThread() {
        check(Thread.currentThread() === thread) { "Native font provider must be used on its owner thread" }
        check(!closed) { "Native font provider is closed" }
    }

    override fun load(id: String, data: ByteArray, faceIndex: Int, version: Long): FontFace {
        checkThread()
        require(id.isNotBlank() && data.isNotEmpty() && faceIndex >= 0 && version >= 0)
        require(id !in faces) { "Font face '$id' is already loaded; use a new identity for replacement" }
        val storage = MemoryUtil.memAlloc(data.size)
        storage.put(data).flip()
        var face: FT_Face? = null
        try {
            face = MemoryStack.stackPush().use { stack ->
                val result = stack.mallocPointer(1)
                checkFreeType(FreeType.FT_New_Memory_Face(library, storage, faceIndex.toLong(), result), "load '$id'")
                FT_Face.create(result[0])
            }
            val native = NativeFace(id, version, face, storage)
            faces[id] = native
            return native
        } catch (failure: Throwable) {
            face?.let { FreeType.FT_Done_Face(it) }
            MemoryUtil.memFree(storage)
            throw failure
        }
    }

    override fun cleanup() {
        check(Thread.currentThread() === thread) { "Native font provider must be closed on its owner thread" }
        if (closed) return
        faces.values.forEach { it.release() }
        faces.clear()
        FreeType.FT_Done_FreeType(library)
        closed = true
    }

    private inner class NativeFace(
        override val id: String,
        override val version: Long,
        private val face: FT_Face,
        private val storage: ByteBuffer
    ) : FontFace {

        private var currentSize = -1f
        private val hbFont: Long

        init {
            setSize(16f)
            hbFont = HarfBuzz.hb_ft_font_create_referenced(face.address())
            check(hbFont != 0L) { "Cannot create HarfBuzz font '$id'" }
            HarfBuzz.hb_ft_font_set_load_flags(hbFont, FreeType.FT_LOAD_NO_HINTING or FreeType.FT_LOAD_NO_BITMAP)
        }

        private fun setSize(size: Float) {
            require(size.isFinite() && size > 0) { "Font size must be positive and finite" }
            if (size == currentSize) return
            checkFreeType(FreeType.FT_Set_Char_Size(face, 0, max(1L, (size * 64.0).roundToLong()), 72, 72), "set size of '$id'")
            currentSize = size
        }

        private fun prepare(size: Float) {
            checkThread()
            if (size != currentSize) {
                setSize(size)
                HarfBuzz.hb_ft_font_changed(hbFont)
            }
        }

        override fun contains(codepoint: Int): Boolean {
            checkThread()
            require(Character.isValidCodePoint(codepoint))
            return FreeType.FT_Get_Char_Index(face, codepoint.toLong()) != 0
        }

        override fun metrics(size: Float): FontMetrics {
            prepare(size)
            val metrics = checkNotNull(face.size()).metrics()
            val ascent = max(0f, metrics.ascender() / 64f)
            val descent = max(0f, -metrics.descender() / 64f)
            return FontMetrics(ascent, descent, max(0f, metrics.height() / 64f - ascent - descent))
        }

        override fun shape(text: String, size: Float, direction: FontDirection, language: String): ShapedRun {
            prepare(size)
            val metrics = metrics(size)
            val buffer = HarfBuzz.hb_buffer_create()
            check(buffer != 0L) { "Cannot allocate HarfBuzz buffer" }
            try {
                HarfBuzz.hb_buffer_set_cluster_level(buffer, HarfBuzz.HB_BUFFER_CLUSTER_LEVEL_MONOTONE_GRAPHEMES)
                HarfBuzz.hb_buffer_add_utf16(buffer, text, 0, text.length)
                HarfBuzz.hb_buffer_guess_segment_properties(buffer)
                HarfBuzz.hb_buffer_set_direction(buffer, if (direction == FontDirection.RTL) HarfBuzz.HB_DIRECTION_RTL else HarfBuzz.HB_DIRECTION_LTR)
                HarfBuzz.hb_buffer_set_language(buffer, HarfBuzz.hb_language_from_string(language))
                HarfBuzz.hb_shape(hbFont, buffer, null)
                val infos = HarfBuzz.hb_buffer_get_glyph_infos(buffer)
                val positions = HarfBuzz.hb_buffer_get_glyph_positions(buffer)
                if (infos == null || positions == null) return ShapedRun(text, direction, size, metrics, emptyList())
                val starts = (0 until infos.remaining()).map { infos[it].cluster() }.distinct().sorted()
                val ends = starts.mapIndexed { index, start -> start to (starts.getOrNull(index + 1) ?: text.length) }.toMap()
                val glyphs = (0 until infos.remaining()).map { index ->
                    val info = infos[index]
                    val position = positions[index]
                    ShapedGlyph(
                        info.codepoint(), info.cluster(), ends.getValue(info.cluster()),
                        position.x_advance() / 64f, -position.y_advance() / 64f,
                        position.x_offset() / 64f, -position.y_offset() / 64f
                    )
                }
                return ShapedRun(text, direction, size, metrics, glyphs)
            } finally {
                HarfBuzz.hb_buffer_destroy(buffer)
            }
        }

        override fun rasterize(glyphId: Int, size: Float, pixelsPerUnit: Float): RasterGlyph {
            require(glyphId >= 0 && pixelsPerUnit.isFinite() && pixelsPerUnit > 0)
            prepare(size * pixelsPerUnit)
            checkFreeType(FreeType.FT_Load_Glyph(face, glyphId, FreeType.FT_LOAD_DEFAULT), "load glyph $glyphId from '$id'")
            val slot = checkNotNull(face.glyph())
            checkFreeType(FreeType.FT_Render_Glyph(slot, FreeType.FT_RENDER_MODE_NORMAL), "rasterize glyph $glyphId from '$id'")
            val bitmap = slot.bitmap()
            val width = bitmap.width()
            val rows = bitmap.rows()
            val pitch = bitmap.pitch()
            require(width.toLong() * rows <= 16L * 1024 * 1024) { "Glyph bitmap exceeds 16MiB budget" }
            if (width == 0 || rows == 0) return RasterGlyph(width, rows, slot.bitmap_left(), slot.bitmap_top(), ByteArray(0))
            check(bitmap.pixel_mode().toInt() == FreeType.FT_PIXEL_MODE_GRAY) { "Unsupported glyph bitmap mode ${bitmap.pixel_mode()}" }
            val source = checkNotNull(bitmap.buffer(abs(pitch) * rows))
            val pixels = ByteArray(width * rows)
            for (y in 0 until rows) {
                val sourceRow = if (pitch >= 0) y * pitch else (rows - y - 1) * -pitch
                for (x in 0 until width) pixels[y * width + x] = source[sourceRow + x]
            }
            return RasterGlyph(width, rows, slot.bitmap_left(), slot.bitmap_top(), pixels)
        }

        fun release() {
            HarfBuzz.hb_font_destroy(hbFont)
            FreeType.FT_Done_Face(face)
            MemoryUtil.memFree(storage)
        }
    }
}

private fun checkFreeType(code: Int, operation: String) {
    check(code == 0) { "Failed to $operation (FreeType error 0x${code.toString(16)})" }
}
