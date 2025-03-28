package arc.asset.shader

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * Represents a fragment shader asset (.fsh file).
 *
 * Fragment shaders are used in the rendering to calculate the color and other attributes
 * of each pixel in a rendered image.
 */
@ImmutableType
interface FragmentShader : ShaderAsset {

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create instance of [FragmentShader] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [FragmentShader].
         */
        fun create(file: File): FragmentShader

    }

    companion object {

        /**
         * Create instance of [FragmentShader] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [FragmentShader].
         */
        @JvmStatic
        fun from(file: File): FragmentShader {
            return Arc.factory<Factory>().create(file)
        }

    }

}