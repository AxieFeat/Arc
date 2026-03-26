package arc.gles.window

import arc.gles.AngleBackend
import arc.window.WindowBackend
import org.lwjgl.egl.EGL10.EGL_NONE
import org.lwjgl.egl.EGL10.EGL_NO_DISPLAY
import org.lwjgl.egl.EGL10.eglGetDisplay
import org.lwjgl.egl.EGL10.eglInitialize
import org.lwjgl.egl.EGL14.EGL_DEFAULT_DISPLAY
import org.lwjgl.egl.EXTPlatformBase.eglGetPlatformDisplayEXT
import org.lwjgl.system.MemoryStack
import kotlin.use

/** EGL_PLATFORM_ANGLE_ANGLE – tells EGL that we want an ANGLE display. */
private const val EGL_PLATFORM_ANGLE_ANGLE = 0x3202

/** EGL_PLATFORM_ANGLE_TYPE_ANGLE – attribute key for the ANGLE backend type. */
private const val EGL_PLATFORM_ANGLE_TYPE_ANGLE = 0x3203

internal object EglWindowBackend : WindowBackend {

    /**
     * The ANGLE backend to use when creating the EGL display.
     * Must be set before [display] is first accessed (i.e. before any window is created).
     */
    @Volatile
    internal var angleBackend: AngleBackend = AngleBackend.DEFAULT

    /**
     * EGL display handle.
     * Initialized lazily so that [angleBackend] can be configured first via [OpenGLES.preload].
     */
    val display: Long by lazy { initializeDisplay() }

    private var _major: Int = 0
    private var _minor: Int = 0

    private fun initializeDisplay(): Long {
        val disp = createDisplay()

        if (disp == EGL_NO_DISPLAY) error("No EGL display")

        MemoryStack.stackPush().use { stack ->
            val major = stack.mallocInt(1)
            val minor = stack.mallocInt(1)

            require(eglInitialize(disp, major, minor)) { "EGL init failed" }

            _major = major[0]
            _minor = minor[0]
        }

        return disp
    }

    private fun createDisplay(): Long {
        if (angleBackend == AngleBackend.DEFAULT) {
            return eglGetDisplay(EGL_DEFAULT_DISPLAY)
        }

        return MemoryStack.stackPush().use { stack ->
            val attribs = stack.mallocInt(3).apply {
                put(EGL_PLATFORM_ANGLE_TYPE_ANGLE); put(angleBackend.eglTypeId)
                put(EGL_NONE)
                flip()
            }

            eglGetPlatformDisplayEXT(EGL_PLATFORM_ANGLE_ANGLE, EGL_DEFAULT_DISPLAY, attribs)
        }
    }

    override val name: String
        get() = "egl"

    override val version: String
        get() {
            display // ensure the display (and EGL version) is initialized
            return "$_major.$_minor"
        }
}
