package arc.shader

import arc.Arc
import arc.annotations.TypeFactory
import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable
import org.jetbrains.annotations.ApiStatus
import java.nio.ByteBuffer
import kotlin.jvm.Throws

/**
 * This interface represents unform buffer for shaders.
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
            return Arc.factory<Factory>().create(size)
        }

    }

}