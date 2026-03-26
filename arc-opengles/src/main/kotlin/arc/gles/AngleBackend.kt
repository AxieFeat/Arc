package arc.gles

/**
 * Represents the ANGLE backend implementation to use for rendering.
 *
 * ANGLE (Almost Native Graphics Layer Engine) translates OpenGL ES calls to native graphics APIs.
 * The backend determines which native API is used under the hood.
 *
 * Pass the desired backend to [OpenGLES.preload] before creating any windows.
 *
 * @see OpenGLES.preload
 */
enum class AngleBackend(internal val eglTypeId: Int) {

    /** Let ANGLE choose the best available backend for the current platform (default behavior). */
    DEFAULT(0x3206),  // EGL_PLATFORM_ANGLE_TYPE_DEFAULT_ANGLE

    /** Use Direct3D 9 backend. Windows only. */
    D3D9(0x3207),     // EGL_PLATFORM_ANGLE_TYPE_D3D9_ANGLE

    /** Use Direct3D 11 backend. Windows only. */
    D3D11(0x3208),    // EGL_PLATFORM_ANGLE_TYPE_D3D11_ANGLE

    /** Use OpenGL backend. */
    OPENGL(0x320D),   // EGL_PLATFORM_ANGLE_TYPE_OPENGL_ANGLE

    /** Use OpenGL ES backend. */
    OPENGLES(0x320E), // EGL_PLATFORM_ANGLE_TYPE_OPENGLES_ANGLE

    /** Use Vulkan backend. */
    VULKAN(0x3450),   // EGL_PLATFORM_ANGLE_TYPE_VULKAN_ANGLE

    /** Use Metal backend. macOS only. */
    METAL(0x3489),    // EGL_PLATFORM_ANGLE_TYPE_METAL_ANGLE

    /** Use the null (no-op) backend. Intended for testing. */
    NULL(0x33AE),     // EGL_PLATFORM_ANGLE_TYPE_NULL_ANGLE
}
