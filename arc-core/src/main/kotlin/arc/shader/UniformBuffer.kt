package arc.shader

import arc.Arc.single
import arc.annotations.TypeFactory
import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable
import org.jetbrains.annotations.ApiStatus
import java.nio.ByteBuffer
import kotlin.jvm.Throws

/**
 * This interface represents unform buffer for shaders.
 *
 * In this example, we create a buffer for the array of light sources.
 *
 * Let's say our shader has a similar block:
 * ```glsl
 * struct Light {
 *     vec4 color;
 *     vec3 position;
 *     float radius;
 * };
 *
 * uniform Lights {
 *     Light lights[128];
 * };
 * ```
 * For providing this array of light sources, we should:
 * ```kotlin
 * // Extra functions for ByteBuffer, used below.
 * fun ByteBuffer.putColor(color: Color) {
 *     this.putFloat(color.red / 255f)
 *     this.putFloat(color.green / 255f)
 *     this.putFloat(color.blue / 255f)
 *     this.putFloat(color.alpha.toFloat())
 * }
 * fun ByteBuffer.putVector(vector: Vector3f) {
 *     this.putFloat(vector.x)
 *     this.putFloat(vector.y)
 *     this.putFloat(vector.z)
 * }
 *
 * // We have struct in glsl shader, and for convenience we will create its analogue in Kotlin
 * data class Light(
 *     val color: Color,
 *     val position: Vector3f,
 *     val radius: Float
 * )
 *
 * // Array of lights for putting.
 * val array = arrayOf(
 *     Light(Color.RED, Vector3f(0f, 5f, -5f), 10f),
 *     Light(Color.GREEN, Vector3f(0f, 1f, 5f), 10f),
 *     Light(Color.BLUE, Vector3f(5f, 1f, 0f), 10f)
 * )
 *
 *
 *
 * // In order to create a UniformBuffer, we need to know its size.
 * // In this example, we use the Vertex format, but you can calculate the sizes of the data types manually.
 *
 * val lightsCount = 128 // Constant with count of lights
 *
 * val size = (
 *     VertexFormatElement.COLOR.size +    // <- It's a vec4 color in struct
 *     VertexFormatElement.POSITION.size + // <- It's a vec3 position
 *     VertexType.FLOAT.size               // <- It's a single float (radius)
 * ) * lightsCount // Multiply by count of lights.
 *
 * // Now we can create buffer.
 * val buffer = UniformBuffer.of(size)
 *
 * // Use lazy function for storing data in buffer.
 * buffer.update {
 *
 *     // We use for each in the range 0..lightsCount because arrays in glsl are always fixed length
 *     for(i in 0..lightsCount) {
 *         val light = array.getOrNull(i)
 *
 *         if (light != null) {
 *             this.putColor(light.color)
 *             this.putVector(light.position)
 *             this.putFloat(light.radius)
 *         } else {
 *             // If we go out the bounds of our light array, then we fill with "zeros"
 *             this.putColor(Color.TRANSPARENT)
 *             this.putVector(Vector3f())
 *             this.putFloat(0f)
 *         }
 *     }
 * }
 *
 * // After all operations, we can use our buffer, yee!
 *
 * val shader: ShaderInstance = ...
 * shader.uploadUniformBuffer("Lights", buffer) // <- Upload our buffer to shader via uniform name.
 * ```
 */
interface UniformBuffer : Cleanable, Bindable {

    /**
     * ID of this buffer in a render system.
     */
    val id: Int

    /**
     * Max size of this uniform buffer in bytes.
     */
    val size: Int

    /**
     * Updates data in buffer with new values.
     *
     * @param data New data for writing to buffer.
     *
     * @throws IllegalArgumentException If size of [data] > [size].
     */
    @Throws(IllegalArgumentException::class)
    fun update(data: ByteBuffer)

    /**
     * Updates data in buffer with new values.
     *
     * @param size Size of buffer.
     * @param data Configuring of buffer. Not use [ByteBuffer.flip] here, it's unnecessary.
     *
     * @throws IllegalArgumentException If size of [data] > [size].
     */
    @Throws(IllegalArgumentException::class)
    fun update(size: Int = this.size, data: ByteBuffer.() -> Unit)

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        fun create(size: Int): UniformBuffer

    }

    companion object {

        /**
         * Create new instance of [UniformBuffer].
         *
         * @param size Size of buffer.
         *
         * @return New instance of [UniformBuffer].
         */
        @JvmStatic
        fun of(size: Int): UniformBuffer {
            return single<Factory>().create(size)
        }

    }

}