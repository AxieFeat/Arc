package arc.gles.window

import arc.window.WindowBackend
import org.lwjgl.egl.EGL10.EGL_NO_DISPLAY
import org.lwjgl.egl.EGL10.eglGetDisplay
import org.lwjgl.egl.EGL10.eglInitialize
import org.lwjgl.egl.EGL14.EGL_DEFAULT_DISPLAY
import org.lwjgl.system.MemoryStack
import kotlin.use

internal object EglWindowBackend : WindowBackend {

    /**
     * We create display instance here because we need it for getting version.
     */
    val display: Long = eglGetDisplay(EGL_DEFAULT_DISPLAY)

    private var major: Int = 0
    private var minor: Int = 0

    init {
        if (display == EGL_NO_DISPLAY) error("No EGL display")

        MemoryStack.stackPush().use { stack ->
            val major = stack.mallocInt(1)
            val minor = stack.mallocInt(1)

            require(eglInitialize(display, major, minor)) { "EGL init failed" }

            this.major = major[0]
            this.minor = minor[0]
        }
    }

    override val name: String
        get() = "egl"

    override val version: String
        get() {
            return "$major.$minor"
        }
}
