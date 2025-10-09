package arc.gles.window

object NativeEGLBridge {

    init {
        System.loadLibrary("NativeEGLBridge")
    }

    /**
     * Get native layer/descriptor of window for EGL.
     *
     * @param windowPtr pointer to native window from GLFW:
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
