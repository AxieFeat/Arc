package arc.shader

import arc.Arc
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
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface ShaderInstance {

    /**
     * ID of this shader in render system.
     */
    @get:JvmName("id")
    val id: Int

    /**
     * The vertex shader associated with this shader instance.
     */
    @get:JvmName("vertex")
    val vertex: VertexShader

    /**
     * The fragment shader associated with this shader instance.
     */
    @get:JvmName("fragment")
    val fragment: FragmentShader

    /**
     * Data of shader.
     */
    @get:JvmName("uniforms")
    val data: ShaderData

    /**
     * Add uniform provider to this shader instance.
     *
     * @param provider Provider to add.
     */
    fun addProvider(provider: UniformProvider)

    /**
     * Set int uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Int)

    /**
     * Set float uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Float)

    /**
     * Set mat4x4 uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Matrix4f)

    /**
     * Set vec4 uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Vector4f)

    /**
     * Set vec3 uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Vector3f)

    /**
     * Set vec2 uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Vector2f)

    /**
     * Compiles the vertex and fragment shaders for this shader instance.
     */
    fun compileShaders()

    /**
     * Binds this shader instance in current context.
     */
    fun bind()

    /**
     * Unbinds this shader instance from the current context.
     */
    fun unbind()

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

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