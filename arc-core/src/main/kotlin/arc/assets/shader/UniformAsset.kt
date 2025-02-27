package arc.assets.shader

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.assets.Asset
import arc.shader.ShaderUniforms
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * This interface represents asset with uniforms for shader.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface UniformAsset : Asset {

    @get:JvmName("uniforms")
    val uniforms: ShaderUniforms

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create instance of [UniformAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [UniformAsset].
         */
        fun create(file: File): UniformAsset

    }

    companion object {

        /**
         * Create instance of [UniformAsset] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [UniformAsset].
         */
        @JvmStatic
        fun from(file: File): UniformAsset {
            return Arc.factory<Factory>().create(file)
        }

    }

}