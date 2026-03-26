package arc.gles

import arc.gles.window.EglWindowBackend
import arc.gles.window.NativeEGLBridge

/**
 * This object represents OpenGL ES implementation of engine.
 */
object OpenGLES {

    /**
     * Preload OpenGL ES implementations.
     *
     * @param backend The ANGLE backend to use for rendering.
     *   Defaults to [AngleBackend.DEFAULT], which lets ANGLE choose the best available backend.
     *   Must be called before any window is created.
     */
    @JvmStatic
    @JvmOverloads
    fun preload(backend: AngleBackend = AngleBackend.DEFAULT) {
        NativeEGLBridge // Call to preload native library
        EglWindowBackend.angleBackend = backend
        GlesFactoryProvider.bootstrap()
    }
}
