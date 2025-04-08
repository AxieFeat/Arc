package arc.graphics.vertex

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents buffer of vertices.
 *
 * @sample arc.sample.meshSample
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface VertexBuffer : Bindable, Cleanable {

    /**
     * ID of this buffer in Render system.
     */
    @get:JvmName("id")
    val id: Int

    /**
     * Vertex format of this buffer.
     */
    @get:JvmName("format")
    val format: VertexFormat

    /**
     * Mode of drawing of this buffer.
     */
    @get:JvmName("mode")
    val mode: DrawerMode

    /**
     * Count of vertices in this buffer.
     */
    @get:JvmName("size")
    val size: Int

    /**
     * Store some [DrawBuffer] in this vertex buffer (It will overwrite old values).
     *
     * @param drawBuffer Draw buffer for writing it this vertex buffer instance.
     */
    fun write(drawBuffer: DrawBuffer)

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(drawBuffer: DrawBuffer): VertexBuffer

    }

    companion object {

        /**
         * Create new instance of [VertexBuffer].
         *
         * @param drawBuffer Draw buffer for writing vertex data.
         *
         * @return New instance of [VertexBuffer].
         */
        @JvmStatic
        fun of(drawBuffer: DrawBuffer): VertexBuffer {
            return Arc.factory<Factory>().create(drawBuffer)
        }

    }

}