package arc.gles

/**
 * Represents the backend implementation used by ANGLE (Almost Native Graphics Layer Engine)
 * to translate OpenGL ES calls to a native graphics API.
 *
 * The backend is selected by passing the desired value to [OpenGLES.preload].
 *
 * @param eglType The EGL platform-angle type attribute value used when creating the EGL display.
 */
enum class AngleBackend(val eglType: Int) {

    /** Let ANGLE automatically select the best available backend for the current platform. */
    DEFAULT(0x3206),

    /** Use Direct3D 9 as the backend. Available on Windows only. */
    D3D9(0x3207),

    /** Use Direct3D 11 as the backend. Available on Windows only. */
    D3D11(0x3208),

    /** Use desktop OpenGL as the backend. */
    OPENGL(0x320D),

    /** Use OpenGL ES as the backend. */
    OPENGLES(0x320E),

    /** Use Vulkan as the backend. */
    VULKAN(0x3450),

    /** Use Metal as the backend. Available on macOS only. */
    METAL(0x3489),

    /**
     * Use the null (no-op) backend.
     * Useful for testing without actual rendering output.
     */
    NULL(0x33AE),
}
