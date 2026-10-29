package arc.graphics

import arc.texture.Texture
import arc.util.pattern.Cleanable

/**
 * Context-owned RGBA8 target. Its color texture is a borrowed view, valid until resize or cleanup.
 * Creation, resize and cleanup require the creating context's render thread. Cleanup is idempotent.
 * Ordinary positioned areas do not require render targets; use these for compositing, masks and effects.
 *
 */
interface RenderTarget : Cleanable, ContextResource {

    /**
     * Current color attachment width in pixels.
     */
    val width: Int

    /**
     * Current color attachment height in pixels.
     */
    val height: Int

    /**
     * Whether a combined depth/stencil attachment is allocated.
     */
    val depthStencil: Boolean

    /**
     * Borrowed color attachment; never clean it up separately.
     */
    val colorTexture: Texture

    /**
     * Reallocates attachments only when dimensions change. Existing contents are discarded.
     * The target must be unbound and must not be referenced by an open pass snapshot during resize/cleanup.
     */
    fun resize(width: Int, height: Int): Boolean
}
