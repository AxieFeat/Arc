package arc.graphics

import arc.texture.Texture
import arc.util.pattern.Cleanable

/**
 * Context-owned RGBA8 target. Its color texture is a borrowed view, valid until resize or cleanup.
 * Creation, resize and cleanup require the creating context's render thread. Cleanup is idempotent.
 * Ordinary positioned areas do not require render targets; use these for compositing, masks and effects.
 */
interface RenderTarget : Cleanable, ContextResource {

    val width: Int
    val height: Int
    val depthStencil: Boolean
    val colorTexture: Texture

    /**
     * Reallocates attachments only when dimensions change. Existing contents are discarded.
     * The target must be unbound and must not be referenced by an open pass snapshot during resize/cleanup.
     */
    fun resize(width: Int, height: Int): Boolean
}
