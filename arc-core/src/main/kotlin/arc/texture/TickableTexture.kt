package arc.texture

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.util.Tickable
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents updatable texture via atlas.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface TickableTexture : TextureLike, Tickable {

    /**
     * Atlas of this tickable texture.
     */
    @get:JvmName("atlas")
    val atlas: TextureAtlas

    /**
     * Tick this texture.
     *
     * @param delta Delta value in this frame.
     */
    override fun tick(delta: Float)

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create [TickableTexture] from [TextureAtlas].
         *
         * @param atlas Atlas for Tickable Texture.
         *
         * @return New instance of [TickableTexture].
         */
        fun create(atlas: TextureAtlas): TickableTexture

    }

    companion object {

        /**
         * Create [TickableTexture] from [TextureAtlas].
         *
         * @param atlas Atlas for Tickable Texture.
         *
         * @return New instance of [TickableTexture].
         */
        @JvmStatic
        fun create(atlas: TextureAtlas): TickableTexture {
            return Arc.factory<Factory>().create(atlas)
        }

    }

}