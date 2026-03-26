package arc.gles

import arc.gles.window.EglConfig
import arc.gles.window.NativeEGLBridge

/**
 * This object represents OpenGL ES implementation of engine backed by Google ANGLE.
 */
object OpenGLES {

    /**
     * Preload OpenGL ES implementations.
     *
     * @param backend The ANGLE backend to use for rendering. Defaults to [AngleBackend.DEFAULT],
     *                which lets ANGLE automatically select the best backend for the current platform.
     *                Must be called before the application window is created.
     */
    @JvmStatic
    fun preload(backend: AngleBackend = AngleBackend.DEFAULT) {
        EglConfig.backend = backend
        NativeEGLBridge // Call to preload native library
        GlesFactoryProvider.bootstrap()
    }
}
