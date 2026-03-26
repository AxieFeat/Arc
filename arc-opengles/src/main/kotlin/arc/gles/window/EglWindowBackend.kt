package arc.gles.window

import arc.gles.AngleBackend
import arc.window.WindowBackend
import org.lwjgl.egl.EGL
import org.lwjgl.egl.EGL10.EGL_NONE
import org.lwjgl.egl.EGL10.EGL_NO_DISPLAY
import org.lwjgl.egl.EGL10.eglGetDisplay
import org.lwjgl.egl.EGL10.eglInitialize
import org.lwjgl.egl.EGL14.EGL_DEFAULT_DISPLAY
import org.lwjgl.system.JNI.callPPP
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.memAddress
import kotlin.use

// ANGLE EGL extension constants (EGL_ANGLE_platform_angle)
private const val EGL_PLATFORM_ANGLE_ANGLE = 0x3202
private const val EGL_PLATFORM_ANGLE_TYPE_ANGLE = 0x3203

/**
 * Internal configuration holder for ANGLE backend selection.
 * Must be set before [EglWindowBackend] is first accessed.
 */
internal object EglConfig {
    var backend: AngleBackend = AngleBackend.DEFAULT
}

internal object EglWindowBackend : WindowBackend {

    /**
     * We create display instance here because we need it for getting version.
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
        val backend = EglConfig.backend
        return if (backend == AngleBackend.DEFAULT) {
            eglGetDisplay(EGL_DEFAULT_DISPLAY)
        } else {
            createAnglePlatformDisplay(backend)
        }
    }

    /**
     * Creates an EGL display using the ANGLE platform extension to select a specific backend.
     *
     * Uses the `EGL_EXT_platform_base` extension via a direct JNI call so that
     * `EGL_DEFAULT_DISPLAY` (0) can be passed as `native_display`, which is valid per
     * the `EGL_ANGLE_platform_angle` extension spec but would be rejected by LWJGL's
     * null-pointer check in the standard binding.
     */
    private fun createAnglePlatformDisplay(backend: AngleBackend): Long {
        return MemoryStack.stackPush().use { stack ->
            val attribs = stack.mallocInt(3).apply {
                put(EGL_PLATFORM_ANGLE_TYPE_ANGLE)
                put(backend.eglType)
                put(EGL_NONE)
                flip()
            }

            // Obtain the eglGetPlatformDisplayEXT function address from the EGL client capabilities.
            // ANGLE exposes this as a client extension (EGL_EXT_platform_base) so it is available
            // before any display has been created.
            val funcAddr = EGL.getCapabilities().eglGetPlatformDisplayEXT
            require(funcAddr != 0L) {
                "eglGetPlatformDisplayEXT is not supported. " +
                    "Ensure ANGLE provides the EGL_EXT_platform_base extension."
            }

            // Call the function directly through LWJGL's low-level JNI helper so that
            // EGL_DEFAULT_DISPLAY (0) can be passed as native_display without triggering
            // LWJGL's null-pointer guard, which rejects 0 even though it is a legal value
            // for this extension.
            callPPP(EGL_PLATFORM_ANGLE_ANGLE, 0L, memAddress(attribs), funcAddr)
        }
    }

    override val name: String
        get() = "egl"

    override val version: String
        get() {
            return "$major.$minor"
        }
}
