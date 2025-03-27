package arc.graphics

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.graphics.vertex.VertexFormat
import org.jetbrains.annotations.ApiStatus

@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface VertexBuffer {

    @get:JvmName("id")
    val id: Int

    @get:JvmName("format")
    val format: VertexFormat

    @get:JvmName("mode")
    val mode: DrawerMode

    @get:JvmName("size")
    val size: Int

    fun bind()

    fun unbind()

    fun bufferData(drawBuffer: DrawBuffer)

    fun cleanup()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(buffer: DrawBuffer): VertexBuffer

    }

    companion object {

        @JvmStatic
        fun create(buffer: DrawBuffer): VertexBuffer {
            return Arc.factory<Factory>().create(buffer)
        }

    }

}