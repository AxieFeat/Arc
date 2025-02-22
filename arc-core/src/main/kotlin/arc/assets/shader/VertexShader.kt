package arc.assets.shader

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * Represents a vertex shader asset (.vsh file).
 *
 * Vertex shaders are utilized in the rendering pipeline to transform vertex data,
 * including position, color, and texture coordinates, and pass it to subsequent stages.
 * This interface extends [ShaderAsset], ensuring the asset type adheres to the system's
 * immutability requirements.
 *
 * Implementations of this interface must provide an immutable representation of a vertex shader file.
 */
@ImmutableType
interface VertexShader : ShaderAsset {

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create instance of [VertexShader] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [VertexShader].
         */
        fun create(file: File): VertexShader

    }

    companion object {

        /**
         * Create instance of [VertexShader] from file.
         *
         * @param file File of asset.
         *
         * @return New instance of [VertexShader].
         */
        @JvmStatic
        fun from(file: File): VertexShader {
            return Arc.factory<Factory>().create(file)
        }

    }

}