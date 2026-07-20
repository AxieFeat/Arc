package arc.gles

import arc.window.egl.NativeEGLBridge

/**
 * This object represents OpenGL implementation of engine.
 */
object OpenGLES {

    /**
     * Preload OpenGL implementations.
     */
    @JvmStatic
    fun preload() {
        NativeEGLBridge // Call to preload native library
        GlesFactoryProvider.bootstrap()
    }
}
