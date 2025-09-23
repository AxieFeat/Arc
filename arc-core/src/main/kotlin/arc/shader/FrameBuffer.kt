package arc.shader

import arc.Arc.single
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents a frame buffer.
 */
interface FrameBuffer {

    /**
     * Width of buffer.
     */
    val width: Int

    /**
     * Height of buffer.
     */
    val height: Int

    /**
     * Texture width in the buffer.
     */
    val textureWidth: Int

    /**
     * Texture height in buffer.
     */
    val textureHeight: Int

    /**
     * Is depth used by this buffer?
     */
    val isUseDepth: Boolean

    /**
     * Bind this buffer for current context.
     *
     * @param viewport Setting viewport to buffer width and height?
     */
    fun bind(viewport: Boolean)

    /**
     * Unbind this buffer from the current context.
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
     * Resize the viewport of this frame buffer.
     *
     * @param width New width.
     * @param height New height.
     *
     * @return `true` if was resized, otherwise `false`.
     */
    fun resize(width: Int, height: Int): Boolean

    /**
     * Clear the current frame buffer.
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
            return single<Factory>().create(width, height, useDepth)
        }
    }
}
