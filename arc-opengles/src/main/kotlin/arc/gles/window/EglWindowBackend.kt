package arc.gles.window

import arc.gles.AngleBackend
import arc.gles.AngleConfig
import arc.window.WindowBackend
import org.lwjgl.egl.EGL10.EGL_NONE
import org.lwjgl.egl.EGL10.EGL_NO_DISPLAY
import org.lwjgl.egl.EGL10.eglGetDisplay
import org.lwjgl.egl.EGL10.eglInitialize
import org.lwjgl.egl.EGL14.EGL_DEFAULT_DISPLAY
import org.lwjgl.egl.EXTPlatformBase.eglGetPlatformDisplayEXT
import org.lwjgl.system.MemoryStack
import kotlin.use

internal object EglWindowBackend : WindowBackend {

    /**
     * The EGL_ANGLE_platform_angle platform identifier.
     * See: https://chromium.googlesource.com/angle/angle/+/refs/heads/main/extensions/EGL_ANGLE_platform_angle.txt
     */
    private const val EGL_PLATFORM_ANGLE_ANGLE = 0x3202

    /**
     * The EGL attribute key used to specify the ANGLE backend type.
     */
    private const val EGL_PLATFORM_ANGLE_TYPE_ANGLE = 0x3203

    /**
     * We create the display instance here because we need it for getting the version.
     * When a specific [AngleBackend] is configured, we use [eglGetPlatformDisplayEXT]
     * to pass ANGLE platform attributes; otherwise we fall back to [eglGetDisplay].
     *
     * **Important:** [OpenGLES.preload] must be called before this object is first accessed
     * (i.e., before any [arc.gles.window.EglGlesWindow] is created) so that the backend
     * selection from [AngleConfig] is picked up correctly.
     */
    val display: Long = createDisplay()

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

    private fun createDisplay(): Long {
        val backend = AngleConfig.backend

        return if (backend == AngleBackend.DEFAULT) {
            eglGetDisplay(EGL_DEFAULT_DISPLAY)
        } else {
            eglGetPlatformDisplayEXT(
                EGL_PLATFORM_ANGLE_ANGLE,
                EGL_DEFAULT_DISPLAY,
                intArrayOf(
                    EGL_PLATFORM_ANGLE_TYPE_ANGLE, backend.eglType,
                    EGL_NONE
                )
            )
        }
    }

    override val name: String
        get() = "egl"

    override val version: String
        get() {
            return "$major.$minor"
        }
}
