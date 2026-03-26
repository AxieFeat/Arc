package arc.gles

/**
 * Internal configuration holder for the ANGLE backend selection.
 *
 * This object is set by [OpenGLES.preload] before the EGL display is initialized,
 * and is read by [arc.gles.window.EglWindowBackend] during its initialization.
 */
internal object AngleConfig {
    @Volatile
    var backend: AngleBackend = AngleBackend.DEFAULT
}
