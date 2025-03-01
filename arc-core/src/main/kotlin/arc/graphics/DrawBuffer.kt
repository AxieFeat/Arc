package arc.graphics

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.graphics.vertex.VertexConsumer
import arc.graphics.vertex.VertexFormat
import arc.shader.ShaderInstance
import org.jetbrains.annotations.ApiStatus

/**
 * DrawBuffer is an interface that extends VertexConsumer and is used for rendering operations.
 * It provides functionality to store and manage vertex data for rendering,
 * and methods to handle drawing operations with specific settings including mode and format.
 *
 * DrawBuffer supports mutable operations and allows for the integration of shaders during the
 * drawing process.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface DrawBuffer : VertexConsumer {

    val isEnded: Boolean

    @get:JvmName("bufferSize")
    val bufferSize: Int

    /**
     * Represents the drawing mode used for rendering operations within the buffer.
     *
     * The mode determines the type of geometric primitives (e.g., triangles, lines, quads) used
     * during rendering, as well as their connectivity and behavior. It is a critical configuration
     * influencing how the vertex data is interpreted and rendered to the screen.
     */
    @get:JvmName("mode")
    val mode: DrawerMode

    /**
     * Represents the format structure of the vertices used in the `DrawBuffer`.
     *
     * This property defines the vertex format, which includes the attributes, their offsets, and
     * other properties necessary for rendering operations. The format is critical for correctly
     * interpreting vertex data and ensuring the `DrawBuffer` communicates properly with the rendering pipeline.
     */
    @get:JvmName("format")
    val format: VertexFormat

    fun end()

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
        fun create(mode: DrawerMode, format: VertexFormat, bufferSize: Int = 2097152): DrawBuffer {
            return Arc.factory<Factory>().create(mode, format, bufferSize)
        }

    }

}