package arc.font

import arc.graphics.vertex.VertexBuffer
import arc.texture.TextureAtlas
import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable

/**
 * This class represents glyph-based font.
 *
 * @param glyphs Glyphs of this font.
 */
data class GlyphFont(
    val glyphs: Set<Glyph>
) : Cleanable, Bindable {

    private var cachedTexture: TextureAtlas? = null

    /**
     * Getter of current texture atlas in font.
     */
    val texture: TextureAtlas
        get() = cachedTexture!!

    /**
     * Prepare texture for this glyph font.
     */
    fun prepareTexture() {
        cachedTexture?.cleanup()

        // TODO
    }

    /**
     * Get glyph from font by name.
     *
     * @param name Name of glyph.
     *
     * @return Instance of [Glyph] or null, if not found.
     */
    fun getGlyph(name: String): Glyph? {
        return glyphs.find { it.name == name }
    }

    /**
     * Get glyph from font by it ordinal number.
     *
     * @param ordinal Ordinal number of glyph.
     *
     * @return Instance of [Glyph] or null, if not found.
     */
    fun getGlyph(ordinal: Int): Glyph? {
        return glyphs.find { it.codepoint == ordinal }
    }

    /**
     * Get glyph from font by char.
     *
     * @param char Char of glyph.
     *
     * @return Instance of [Glyph] or null, if not found.
     */
    fun getGlyph(char: Char): Glyph? {
        return glyphs.find { it.char == char }
    }

    /**
     * Prepares [VertexBuffer] for rendering [WrappedComponent].
     *
     * @param wrappedComponent Wrapped component for preparing.
     *
     * @return Instance of [VertexBuffer] with vertex data.
     */
    fun prepareComponent(wrappedComponent: WrappedComponent): VertexBuffer {
        TODO()
    }

    /**
     * Cleanup resources of this font.
     */
    override fun cleanup() {
        cachedTexture?.cleanup()
        cachedTexture = null
    }

    /**
     * Binds this font. You need bind this font for rendering any buffer, that generated via [prepareComponent]
     */
    override fun bind() {
        cachedTexture?.bind()
    }

    /**
     * Unbind this font.
     */
    override fun unbind() {
        cachedTexture?.unbind()
    }

}