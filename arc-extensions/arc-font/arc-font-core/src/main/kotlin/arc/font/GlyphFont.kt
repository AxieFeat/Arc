package arc.font

import arc.Arc
import arc.annotations.TypeFactory
import arc.graphics.Drawer
import arc.graphics.vertex.VertexBuffer
import arc.texture.TextureAtlas
import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable
import org.jetbrains.annotations.ApiStatus
import org.joml.Matrix4f

/**
 * This interface represents glyph-based font.
 */
interface GlyphFont : Bindable, Cleanable {

    /**
     * Drawer for preparing texts.
     */
    val drawer: Drawer

    /**
     * All glyphs in this font.
     */
    val glyphs: List<Glyph>

    /**
     * Texture atlas with all glyphs.
     */
    val atlas: TextureAtlas

    /**
     * Get glyph by some char.
     *
     * @param char Char of glyph.
     *
     * @return Instance of [Glyph] or null, if not found.
     */
    fun getGlyph(char: Char): Glyph?

    /**
     * Get glyph by name.
     *
     * @param name Name of glyph.
     *
     * @return Instance of [Glyph] or null, if not found.
     */
    fun getGlyph(name: String): Glyph?

    /**
     * Get glyph by ordinal number.
     *
     * @param codepoint Ordinal point.
     *
     * @return Instance of [Glyph] or null, if not found.
     */
    fun getGlyph(codepoint: Int): Glyph?

    /**
     * Generate [VertexBuffer] for some text.
     *
     * @param matrix Matrix for transformation.
     * @param text Text for preparing.
     *
     * @return Instance of [VertexBuffer].
     */
    fun prepare(matrix: Matrix4f, text: String): VertexBuffer

    override fun bind() { atlas.bind() }
    override fun unbind() { atlas.unbind() }
    override fun cleanup() { atlas.cleanup() }

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(drawer: Drawer, glyphs: List<Glyph>): GlyphFont

    }

    companion object {

        /**
         * Create glyph font by list of glyphs.
         *
         * @param drawer Drawer for preparing texts.
         * @param glyphs Glyphs of font.
         *
         * @return New instance of [GlyphFont].
         */
        @JvmStatic
        fun from(drawer: Drawer, glyphs: List<Glyph>): GlyphFont {
            return Arc.factory<Factory>().create(drawer, glyphs)
        }

    }

}