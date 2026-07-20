package arc.window.egl

import arc.window.WindowBackend
import org.lwjgl.egl.EGL10
import org.lwjgl.egl.EGL10.EGL_NO_DISPLAY

internal object EglWindowBackend : WindowBackend {

    override val name: String = "egl"

    override val version: String = EGL10.eglQueryString(EGL_NO_DISPLAY, EGL10.EGL_VERSION)
        ?: error("No EGL version. Why? Make sure, that loaded EGL library is not corrupted.")
}
