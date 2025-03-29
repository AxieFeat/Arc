package arc.shader

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents frame buffer.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface FrameBuffer {

    /**
     * Width of buffer.
     */
    @get:JvmName("width")
    val width: Int

    /**
     * Height of buffer.
     */
    @get:JvmName("height")
    val height: Int

    /**
     * Texture width in buffer.
     */
    @get:JvmName("textureWidth")
    val textureWidth: Int

    /**
     * Texture height in buffer.
     */
    @get:JvmName("textureHeight")
    val textureHeight: Int

    /**
     * Is depth used by this buffer.
     */
    @get:JvmName("useDepth")
    val useDepth: Boolean

    /**
     * Bind this buffer for current context.
     *
     * @param viewport Setting viewport to buffer width and height?
     */
    fun bind(viewport: Boolean)

    /**
     * Unbind this buffer from current context.
     */
    fun unbind()

    /**
     * Bind texture of buffer for current context.
     */
    fun bindTexture()

    /**
     * Unbind texture of buffer from current context.
     */
    fun unbindTexture()

    /**
     * Clear colors values.
     *
     * @param red Red value.
     * @param green Green value.
     * @param blue Blue value.
     * @param alpha Alpha value.
     */
    fun setColor(red: Float, green: Float, blue: Float, alpha: Float)

    /**
     * Cleanup resources of this buffer.
     */
    fun cleanup()

    /**
     * Render this frame buffer.
     *
     * @param width Viewport width.
     * @param height Viewport height.
     */
    fun render(width: Int = this.width, height: Int = this.height)

    /**
     * Resize viewport of this frame buffer.
     *
     * @param width New width.
     * @param height New height.
     */
    fun resize(width: Int, height: Int)

    /**
     * Clear current frame buffer.
     */
    fun clear()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(width: Int, height: Int, useDepth: Boolean): FrameBuffer

    }

    companion object {

        /**
         * Create new instance of [FrameBuffer].
         *
         * @param width Viewport width.
         * @param height Viewport height.
         * @param useDepth Is buffer use depth buffer.
         *
         * @return New instance of [FrameBuffer].
         */
        @JvmStatic
        fun create(width: Int, height: Int, useDepth: Boolean): FrameBuffer {
            return Arc.factory<Factory>().create(width, height, useDepth)
        }

    }
}