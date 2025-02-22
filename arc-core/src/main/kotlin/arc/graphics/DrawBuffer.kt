package arc.graphics

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.graphics.vertex.VertexConsumer
import arc.graphics.vertex.VertexFormat
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

    /**
     * Draws the current buffer using the preconfigured rendering states and attributes.
     *
     * This method assumes that the buffer has been properly prepared,
     * and it renders the contents of the buffer using internal state variables like `mode` and `format`.
     * It is typically used for rendering shapes, models, or other graphical elements.
     */
    fun draw()

    /**
     * Draws graphical content using the given shader instance.
     *
     * This method utilizes the provided `ShaderInstance` to control the rendering
     * pipeline. The shader instance should be properly compiled and bound before calling
     * this method. The method assumes that the internal buffer and state (e.g., mode, format)
     * of the `DrawBuffer` class have been configured.
     *
     * @param shaderInstance The shader instance to be used for rendering. It defines how
     *                       vertex and fragment data is processed during the rendering pipeline.
     */
    fun draw(shaderInstance: ShaderInstance)

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create new instance of [DrawBuffer].
         *
         * @param mode Draw mode.
         * @param format Vertex setting.
         *
         * @return New instance of [DrawBuffer]
         */
        fun create(mode: DrawerMode, format: VertexFormat): DrawBuffer

    }

    companion object {

        /**
         * Create new instance of [DrawBuffer].
         *
         * @param mode Draw mode.
         * @param format Vertex setting.
         *
         * @return New instance of [DrawBuffer]
         */
        @JvmStatic
        fun create(mode: DrawerMode, format: VertexFormat): DrawBuffer {
            return Arc.factory<Factory>().create(mode, format)
        }

    }

}