package arc.texture

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.assets.TextureAsset
import org.jetbrains.annotations.ApiStatus
import kotlin.jvm.Throws

/**
 * This interface represents texture atlas.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface TextureAtlas : TextureLike {

    /**
     * Width of texture.
     */
    @get:JvmName("width")
    val width: Int

    /**
     * Height of texture.
     */
    @get:JvmName("height")
    val height: Int

    /**
     * Count of rows for this atlas.
     */
    @get:JvmName("rows")
    val rows: Int

    /**
     * Count of columns for this atlas.
     */
    @get:JvmName("columns")
    val columns: Int

    /**
     * Asset of this atlas.
     */
    @get:JvmName("asset")
    val asset: TextureAsset

    /**
     * Get U coordinate for some position in this atlas.
     *
     * @param row Row number.
     * @param column Column number.
     *
     * @throws IllegalArgumentException If U coordinate not found by this row and column.
     */
    @Throws(IllegalArgumentException::class)
    fun u(row: Int, column: Int): Float

    /**
     * Get V coordinate for some position in this atlas.
     *
     * @param row Row number.
     * @param column Column number.
     *
     * @throws IllegalArgumentException If U coordinate not found by this row and column.
     */
    @Throws(IllegalArgumentException::class)
    fun v(row: Int, column: Int): Float

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create [TextureAtlas] from [TextureAsset].
         *
         * @param asset Asset for Texture Atlas.
         * @param rows Count of rows for Texture Atlas.
         * @param columns Count of columns for Texture Atlas.
         *
         * @return New instance of [TextureAtlas].
         */
        fun from(asset: TextureAsset, rows: Int, columns: Int): TextureAtlas

    }

    companion object {

        /**
         * Create [TextureAtlas] from [TextureAsset].
         *
         * @param asset Asset for Texture Atlas.
         * @param rows Count of rows for Texture Atlas.
         * @param columns Count of columns for Texture Atlas.
         *
         * @return New instance of [TextureAtlas].
         */
        @JvmStatic
        fun from(asset: TextureAsset, rows: Int, columns: Int): TextureAtlas {
            return Arc.factory<Factory>().from(asset, rows, columns)
        }

    }

}