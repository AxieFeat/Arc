package arc.font

/**
 * This class represents some glyph in glyph-based fonts.
 *
 * @param char The symbol this glyph represents.
 * @param name Name of this glyph.
 * @param codepoint Glyph ordinal number.
 * @param pattern Patter of this glyph, used for texture generation.
 */
data class Glyph(
    val char: Char = ' ',
    val name: String = "",
    val codepoint: Int = 0,
    val pattern: List<List<Boolean>> = emptyList()
) {

    /**
     * Width of this glyph.
     */
    val width: Int
      get() = pattern[0].size

    /**
     * Height of this glyph.
     */
    val height: Int
        get() = pattern.size

}