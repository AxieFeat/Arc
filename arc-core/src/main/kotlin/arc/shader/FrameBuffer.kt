package arc.shader

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface FrameBuffer {

    @get:JvmName("width")
    val width: Int
    @get:JvmName("height")
    val height: Int

    @get:JvmName("textureWidth")
    val textureWidth: Int
    @get:JvmName("textureHeight")
    val textureHeight: Int

    @get:JvmName("useDepth")
    var useDepth: Boolean

    fun bind(viewport: Boolean)

    fun unbind()

    fun bindTexture()

    fun unbindTexture()

    fun setColor(red: Float, green: Float, blue: Float, alpha: Float)

    fun delete()

    fun render(width: Int = this.width, height: Int = this.height)

    fun resize(width: Int, height: Int)

    fun clear()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(width: Int, height: Int, useDepth: Boolean): FrameBuffer

    }

    companion object {

        @JvmStatic
        fun create(width: Int, height: Int, useDepth: Boolean): FrameBuffer {
            return Arc.factory<Factory>().create(width, height, useDepth)
        }

    }
}