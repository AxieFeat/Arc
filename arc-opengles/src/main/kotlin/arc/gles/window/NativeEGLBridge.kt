package arc.gles.window

/**
 * This native bridge provides functionality to get native layer/descriptor
 * of EGL surface from a given pointer to a GLFW window.
 *
 * It created because LWJGL does not provide such functionality out of the box.
 */
object NativeEGLBridge {

    init {
        System.loadLibrary("NativeEGLBridge")
    }

    /**
     * Get native layer/descriptor of window for EGL.
     *
     * @param windowPtr Pointer to native window from GLFW:
     *                  - macOS: NSWindow*
     *                  - Linux: X11 Window or wl_surface*
     *                  - Windows: HWND
     *
     * @return On macOS - pointer to CAMetalLayer,
     *         On Linux - same as input (X11 Window or wl_surface*),
     *         On Windows - same as input (HWND).
     */
    @JvmStatic
    external fun getNativeLayer(windowPtr: Long): Long
}
