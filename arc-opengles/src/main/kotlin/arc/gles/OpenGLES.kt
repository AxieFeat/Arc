package arc.gles

import arc.gles.window.NativeEGLBridge

/**
 * This object represents the OpenGL ES implementation of the engine via ANGLE.
 */
object OpenGLES {

    /**
     * Preload OpenGL ES implementations.
     *
     * @param angleBackend The ANGLE backend to use for rendering.
     *                     Defaults to [AngleBackend.DEFAULT], which lets ANGLE
     *                     automatically choose the best backend for the current platform.
     */
    @JvmStatic
    @JvmOverloads
    fun preload(angleBackend: AngleBackend = AngleBackend.DEFAULT) {
        AngleConfig.backend = angleBackend
        NativeEGLBridge // Call to preload native library
        GlesFactoryProvider.bootstrap()
    }
}
