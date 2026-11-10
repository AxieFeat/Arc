package arc.shader

import kotlin.jvm.JvmStatic

import arc.Arc.single
import arc.annotations.TypeFactory
import arc.asset.StringAsset
import arc.graphics.ContextResource
import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable
import arc.annotations.InternalApi
import korlibs.math.geom.Matrix3
import korlibs.math.geom.Matrix4
import korlibs.math.geom.Vector2F
import korlibs.math.geom.Vector3F
import korlibs.math.geom.Vector4F

/**
 * Represents an instance of a shader program that combines a vertex shader and a fragment shader.
 *
 * @sample arc.sample.shaderInstanceSample
 */
interface ShaderInstance : Bindable, Cleanable, ContextResource {

    /**
     * ID of this shader in a render system.
     */
    val id: Int

    /**
     * The vertex shader associated with this shader instance.
     */
    val vertex: String

    /**
     * The fragment shader associated with this shader instance.
     */
    val fragment: String

    /**
     * Settings of shader.
     */
    val settings: ShaderSettings

    /**
     * Add a uniform provider to this shader instance.
     *
     * @param provider Provider to add.
     */
    fun addProvider(provider: UniformProvider)

    /**
     * Set bool uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Boolean)

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
     * Set mat3x3 uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Matrix3)

    /**
     * Set mat4x4 uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Matrix4)

    /**
     * Set vec4 uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Vector4F)

    /**
     * Set vec3 uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Vector3F)

    /**
     * Set vec2 uniform for this shader.
     *
     * @param name Name in shader.
     * @param value Value to set.
     */
    fun setUniform(name: String, value: Vector2F)

    /**
     * Uploads uniform buffer for this shader.
     *
     * @param name Name of uniform block in shader.
     * @param buffer Buffer to upload.
     */
    fun uploadUniformBuffer(name: String, buffer: UniformBuffer)

    /**
     * Compiles the vertex and fragment shaders for this shader instance.
     */
    fun compileShaders()

    @TypeFactory
    @InternalApi
    interface Factory {

        fun create(
            vertexShader: StringAsset,
            fragmentShader: StringAsset,
            shaderSettings: ShaderSettings
        ): ShaderInstance
    }

    companion object {

        /**
         * Create new instance of [ShaderInstance].
         *
         * @param vertexShader Vertex shader for instance.
         * @param fragmentShader Fragment shader for instance.
         * @param shaderSettings Settings of shader.
         *
         * @return New instance of [ShaderInstance].
         */
        @JvmStatic
        fun of(
            vertexShader: StringAsset,
            fragmentShader: StringAsset,
            shaderSettings: ShaderSettings = ShaderSettings.of()
        ): ShaderInstance {
            return single<Factory>().create(vertexShader, fragmentShader, shaderSettings)
        }
    }
}
