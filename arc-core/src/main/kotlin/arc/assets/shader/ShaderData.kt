package arc.assets.shader

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.shader.BlendMode
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * This interface represents asset with shader data.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface ShaderData : ShaderAsset {

    /**
     * List of uniforms for this shader.
     * You can use this list for providing uniforms to shader.
     */
    @get:JvmName("uniforms")
    val uniforms: List<String>

    @get:JvmName("blendMode")
    val blendMode: BlendMode?

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create instance of [ShaderData] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [ShaderData].
         */
        fun create(file: File): ShaderData

    }

    companion object {

        /**
         * Create instance of [ShaderData] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [ShaderData].
         */
        @JvmStatic
        fun from(file: File): ShaderData {
            return Arc.factory<Factory>().create(file)
        }

    }

}