package arc.gles

/**
 * Represents the backend implementation used by Google ANGLE.
 *
 * ANGLE (Almost Native Graphics Layer Engine) supports multiple graphics API
 * backends. Use this enum to select the desired one when calling [OpenGLES.preload].
 *
 * @property eglType The EGL platform type constant from the `EGL_ANGLE_platform_angle` extension.
 */
enum class AngleBackend(internal val eglType: Int) {

    /** ANGLE automatically selects the best available backend for the current platform. */
    DEFAULT(0x3206),

    /** Direct3D 9 backend. Available on Windows only. */
    D3D9(0x3207),

    /** Direct3D 11 backend. Available on Windows only. */
    D3D11(0x3208),

    /** Desktop OpenGL backend. */
    OPENGL(0x320C),

    /** OpenGL ES backend. */
    OPENGL_ES(0x320D),

    /** Vulkan backend. */
    VULKAN(0x3450),

    /** Metal backend. Available on macOS only. */
    METAL(0x3489)
}
