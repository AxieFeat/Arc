package arc.gl

import arc.gl.graphics.GlRenderContext
import arc.graphics.RenderContext
import org.lwjgl.opengl.GL

/**
 * This object represents OpenGL implementation of engine.
 */
object OpenGL {

    /**
     * Render services for an externally owned current OpenGL context after GL.createCapabilities().
     * The caller owns polling/presentation and context lifetime; Application's beginFrame/endFrame are not used.
     * Arc's existing OpenGL provider supports one active context per render thread.
     */
    @JvmStatic
    fun currentRenderContext(): RenderContext {
        GL.getCapabilities()
        return GlRenderContext
    }

    /**
     * Preload OpenGL implementations.
     */
    @JvmStatic
    fun preload() {
        GlFactoryProvider.bootstrap()
    }
}
