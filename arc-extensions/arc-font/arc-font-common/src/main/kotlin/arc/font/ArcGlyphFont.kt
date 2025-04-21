package arc.font

import arc.graphics.Drawer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.texture.TextureAtlas
import org.joml.Matrix4f
import java.nio.ByteBuffer
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.sqrt

internal class ArcGlyphFont(
    override val drawer: Drawer,
    override val glyphs: List<Glyph>
) : GlyphFont {

    private val glyphUVs: Map<Glyph, GlyphUV>
    override val atlas: TextureAtlas

    init {
        val (pixels, width, height, uvData) = generateAtlasData(glyphs)

        this.atlas = createTextureAtlas(pixels, width, height)
        this.glyphUVs = uvData
    }

    override fun prepare(matrix: Matrix4f, text: String): VertexBuffer {
        return drawer.begin(DrawerMode.TRIANGLES, format, text.length * 100).use { buffer ->
            var cursorX = 0f

            for (char in text) {
                val glyph = getGlyph(char) ?: continue
                val uv = glyphUVs[glyph] ?: continue

                val x = cursorX
                val y = 0f
                val width = glyph.width
                val height = glyph.height

                val (u0, v1) = atlas.uv(uv.x, uv.y)
                val (u1, v0) = atlas.uv(uv.x + uv.width, uv.y + uv.height)

                buffer.addVertex(matrix, x, y + height, 0f).setTexture(u0, v1)
                buffer.addVertex(matrix, x, y, 0f).setTexture(u0, v0)
                buffer.addVertex(matrix, x + width, y, 0f).setTexture(u1, v0)

                buffer.addVertex(matrix, x, y + height, 0f).setTexture(u0, v1)
                buffer.addVertex(matrix, x + width, y, 0f).setTexture(u1, v0)
                buffer.addVertex(matrix, x + width, y + height, 0f).setTexture(u1, v1)

                cursorX += width
            }

            buffer.build()
        }
    }

    override fun getGlyph(char: Char): Glyph? = glyphs.find { it.char == char }
    override fun getGlyph(name: String): Glyph? = glyphs.find { it.name == name }
    override fun getGlyph(codepoint: Int): Glyph? = glyphs.find { it.codepoint == codepoint }

    data class GlyphUV(
        val glyph: Glyph,
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int
    )

    object Factory : GlyphFont.Factory {
        override fun create(
            drawer: Drawer,
            glyphs: List<Glyph>
        ): GlyphFont {
            return ArcGlyphFont(drawer, glyphs)
        }
    }

    companion object {
        private val format = VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.UV)
            .build()

        private fun calculateAtlasSize(images: Collection<Glyph>): Pair<Int, Int> {
            val totalArea = images.sumOf { it.width * it.height }
            val side = ceil(sqrt(totalArea.toDouble())).toInt()

            return nextPowerOfTwo(side) to nextPowerOfTwo(side)
        }

        private fun nextPowerOfTwo(n: Int): Int {
            var power = 1
            while (power < n) {
                power *= 2
            }
            return power.coerceAtMost(8192)
        }

        private fun generateAtlasData(glyphs: List<Glyph>): AtlasData {
            val (width, height) = calculateAtlasSize(glyphs)
            val pixels = IntArray(width * height)

            val uvMap = mutableMapOf<Glyph, GlyphUV>()
            var x = 0
            var y = 0
            var rowHeight = 0

            for (glyph in glyphs.sortedByDescending { it.height }) {
                if (x + glyph.width > width) {
                    x = 0
                    y += rowHeight
                    rowHeight = 0
                }

                for (gy in 0 until glyph.height) {
                    for (gx in 0 until glyph.width) {
                        val pos = (y + gy) * width + (x + gx)
                        val filled = glyph.pattern.getOrNull(gy)?.getOrNull(gx) == true
                        pixels[pos] = if (filled) 0xFFFFFFFF.toInt() else 0x00000000.toInt()
                    }
                }

                uvMap[glyph] = GlyphUV(glyph, x, y, glyph.width, glyph.height)
                x += glyph.width
                rowHeight = max(rowHeight, glyph.height)
            }

            return AtlasData(pixels, width, height, uvMap)
        }

        private fun createTextureAtlas(pixels: IntArray, width: Int, height: Int): TextureAtlas {
            val buffer = ByteBuffer.allocateDirect(width * height * 4)

            pixels.forEach { pixel ->
                buffer.put((pixel shr 16 and 0xFF).toByte()) // R
                buffer.put((pixel shr 8 and 0xFF).toByte())  // G
                buffer.put((pixel and 0xFF).toByte())        // B
                buffer.put((pixel shr 24 and 0xFF).toByte()) // A
            }
            buffer.flip()

            return TextureAtlas.raw(buffer, width, height)
        }

        private data class AtlasData(
            val pixels: IntArray,
            val width: Int,
            val height: Int,
            val uvMap: Map<Glyph, GlyphUV>
        )
    }
}