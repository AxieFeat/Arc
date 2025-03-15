package arc.graphics

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.graphics.vertex.VertexConsumer
import arc.graphics.vertex.VertexFormat
import arc.shader.ShaderInstance
import org.jetbrains.annotations.ApiStatus

/**
 * DrawBuffer is an interface that used for rendering operations.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface DrawBuffer : VertexConsumer {

    /**
     * Size of buffer.
     */
    @get:JvmName("bufferSize")
    val bufferSize: Int

    /**
     * Represents the drawing mode used for rendering operations within the buffer.
     */
    @get:JvmName("mode")
    val mode: DrawerMode

    /**
     * Represents the format structure of the vertices used in the `DrawBuffer`.
     */
    @get:JvmName("format")
    val format: VertexFormat

    /**
     * End writing to buffer.
     *
     * @return Current instance of [DrawBuffer].
     */
    fun end(): DrawBuffer

    fun cleanup()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create new instance of [DrawBuffer].
         *
         * @param mode Draw mode.
         * @param format Vertex setting.
         * @param bufferSize Size for buffer.
         *
         * @return New instance of [DrawBuffer]
         */
        fun create(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer

    }

    companion object {

        /**
         * Create new instance of [DrawBuffer].
         *
         * @param mode Draw mode.
         * @param format Vertex setting.
         * @param bufferSize Size for buffer.
         *
         * @return New instance of [DrawBuffer]
         */
        @JvmStatic
        fun create(mode: DrawerMode, format: VertexFormat, bufferSize: Int = 256): DrawBuffer {
            return Arc.factory<Factory>().create(mode, format, bufferSize)
        }

    }

}