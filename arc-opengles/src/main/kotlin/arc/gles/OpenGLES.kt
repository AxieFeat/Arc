package arc.gles

import arc.gles.window.NativeEGLBridge

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
