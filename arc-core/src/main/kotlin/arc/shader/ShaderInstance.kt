package arc.shader

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.asset.shader.FragmentShader
import arc.asset.shader.ShaderData
import arc.asset.shader.VertexShader
import org.jetbrains.annotations.ApiStatus
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f

/**
 * Represents an instance of a shader program that combines a vertex shader and a fragment shader.
 * This interface provides methods for compiling, binding, and unbinding shader programs during
 * the rendering process.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface ShaderInstance {

    /**
     * ID of this shader in render system.
     */
    @get:JvmName("id")
    val id: Int

    /**
     * The vertex shader associated with this shader instance.
     *
     * A vertex shader is responsible for processing vertex data, which includes transformations,
     * calculations of positions, colors, and texture coordinates, as part of the rendering pipeline.
     * It acts as the first programmable stage in the GPU rendering process and prepares data for
     * subsequent stages such as the fragment shader.
     */
    @get:JvmName("vertex")
    val vertex: VertexShader

    /**
     * Represents the fragment shader associated with this shader instance.
     *
     * A fragment shader is responsible for handling the pixel-level calculation of color, lighting,
     * and other effects in the rendering pipeline. This property provides access to the fragment shader
     * used by the shader instance during rendering operations.
     */
    @get:JvmName("fragment")
    val fragment: FragmentShader

    /**
     * Data of shader.
     */
    @get:JvmName("uniforms")
    val data: ShaderData

    fun addProvider(provider: UniformProvider)

    fun setUniform(name: String, value: Int)

    fun setUniform(name: String, value: Float)

    fun setUniform(name: String, value: Matrix4f)

    fun setUniform(name: String, value: Vector4f)

    fun setUniform(name: String, value: Vector3f)

    fun setUniform(name: String, value: Vector2f)

    /**
     * Compiles the vertex and fragment shaders for this shader instance.
     *
     * This method processes the attached vertex and fragment shader code,
     * transforming them into a format suitable for use by the graphics pipeline.
     * It ensures that the shaders are valid and ready to be linked in preparation
     * for rendering operations.
     *
     * This function should be called before attempting to bind or use the shader in rendering.
     * If the compilation fails, the application may throw an error or log relevant details.
     */
    fun compileShaders()

    /**
     * Binds this shader instance for use in rendering operations.
     *
     * This method prepares the shader pipeline for rendering by activating the compiled
     * vertex and fragment shaders. It ensures that the graphics pipeline uses this shader instance
     * to process subsequent rendering calls. The shader must be successfully compiled before calling
     * this method, as binding requires the compiled shader program.
     */
    fun bind()

    /**
     * Unbinds this shader instance from the graphics pipeline.
     *
     * This method deactivates the currently bound shader, ensuring that it is no longer
     * used for subsequent rendering operations. After calling this function, the graphics
     * pipeline will not reference the previously bound shader until another shader is
     * explicitly bound.
     */
    fun unbind()

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        /**
         * Create new instance of [ShaderInstance].
         *
         * @param vertexShader Vertex shader for instance.
         * @param fragmentShader Fragment shader for instance.
         * @param shaderData Data of shader.
         *
         * @return New instance of [ShaderInstance].
         */
        fun create(
            vertexShader: VertexShader,
            fragmentShader: FragmentShader,
            shaderData: ShaderData
        ): ShaderInstance

    }

    companion object {

        /**
         * Create new instance of [ShaderInstance].
         *
         * @param vertexShader Vertex shader for instance.
         * @param fragmentShader Fragment shader for instance.
         * @param shaderData Data of shader.
         *
         * @return New instance of [ShaderInstance].
         */
        @JvmStatic
        fun of(
            vertexShader: VertexShader,
            fragmentShader: FragmentShader,
            shaderData: ShaderData
        ): ShaderInstance {
            return Arc.factory<Factory>().create(vertexShader, fragmentShader, shaderData)
        }

    }

}