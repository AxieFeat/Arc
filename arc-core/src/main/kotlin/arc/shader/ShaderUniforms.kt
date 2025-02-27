package arc.shader

import arc.Arc
import arc.annotations.TypeFactory
import arc.assets.shader.UniformAsset
import org.jetbrains.annotations.ApiStatus

interface ShaderUniforms {

    val asset: UniformAsset

    val uniforms: List<Uniform>

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create instance of [ShaderUniforms] from [UniformAsset].
         *
         * @param asset Asset of uniforms.
         *
         * @return New instance of [ShaderUniforms].
         */
        fun create(asset: UniformAsset): ShaderUniforms

    }

    companion object {

        /**
         * Create instance of [ShaderUniforms] from [UniformAsset].
         *
         * @param asset Asset of uniforms.
         *
         * @return New instance of [ShaderUniforms].
         */
        @JvmStatic
        fun from(asset: UniformAsset): ShaderUniforms {
            return Arc.factory<Factory>().create(asset)
        }

    }

}