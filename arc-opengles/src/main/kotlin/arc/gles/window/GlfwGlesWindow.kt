package arc.gles.window

import arc.window.AbstractGlfwWindow
import arc.window.Window
import arc.window.WindowHandler
import org.lwjgl.egl.EGL10.*
import org.lwjgl.egl.EGL12.EGL_RENDERABLE_TYPE
import org.lwjgl.egl.EGL13.EGL_CONTEXT_CLIENT_VERSION
import org.lwjgl.egl.EGL13.EGL_OPENGL_ES2_BIT
import org.lwjgl.egl.EGL14.EGL_DEFAULT_DISPLAY
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWNativeCocoa.glfwGetCocoaView
import org.lwjgl.glfw.GLFWNativeWin32.glfwGetWin32Window
import org.lwjgl.glfw.GLFWNativeX11.glfwGetX11Window
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.Platform
import java.nio.IntBuffer

internal class GlfwGlesWindow(
    name: String,
    handler: WindowHandler,
    width: Int,
    height: Int,
    isResizable: Boolean
) : AbstractGlfwWindow(name, handler, width, height, isResizable) {

    private val display: Long = eglGetDisplay(EGL_DEFAULT_DISPLAY)
    private var surface: Long = -1
    private var context: Long = -1
    private var config: Long = -1

    init {
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API)
    }

    override fun create() {
        super.create()

        MemoryStack.stackPush().use { stack ->
            if (display == EGL_NO_DISPLAY) error("No EGL display")

            val major = stack.mallocInt(1)
            val minor = stack.mallocInt(1)

            require(eglInitialize(display, major, minor)) { "EGL init failed" }

            val eglAttribs = stack.mallocInt(17).apply {
                put(EGL_RED_SIZE); put(8)
                put(EGL_GREEN_SIZE); put(8)
                put(EGL_BLUE_SIZE); put(8)
                put(EGL_ALPHA_SIZE); put(8)
                put(EGL_DEPTH_SIZE); put(24)
                put(EGL_STENCIL_SIZE); put(8)
                put(EGL_RENDERABLE_TYPE); put(EGL_OPENGL_ES2_BIT)
                put(EGL_SURFACE_TYPE);  put(EGL_WINDOW_BIT)
                put(EGL_NONE)
                flip()
            }

            val numConfigs = stack.mallocInt(1)
            val configs = stack.mallocPointer(1)

            require(eglChooseConfig(display, eglAttribs, configs, numConfigs)) {
                "eglChooseConfig failed: ${eglGetError()}"
            }

            require(numConfigs[0] != 0) {
                "No suitable EGL config found for ANGLE"
            }

            config = configs[0]

            val ctxAttribs = stack.mallocInt(3).apply {
                put(EGL_CONTEXT_CLIENT_VERSION); put(2)
                put(EGL_NONE)
                flip()
            }

            context = eglCreateContext(display, config, EGL_NO_CONTEXT, ctxAttribs)
            require(context != EGL_NO_CONTEXT) { "eglCreateContext failed: ${eglGetError()}" }

            val nativeWindow = when(Platform.get()) {
                Platform.MACOSX -> glfwGetCocoaView(handle)
                Platform.WINDOWS -> glfwGetWin32Window(handle)
                Platform.LINUX -> glfwGetX11Window(handle)
                else -> error("Unsupported platform")
            }

            surface = eglCreateWindowSurface(display, config, nativeWindow, null as IntBuffer?)

            require(surface != EGL_NO_SURFACE) {
                "eglCreateWindowSurface failed: 0x${eglGetError().toString(16)}"
            }

            require(eglMakeCurrent(display, surface, surface, context)) {
                "eglMakeCurrent failed: ${eglGetError()}"
            }
        }
    }

    override fun close() {
        eglMakeCurrent(display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT)
        if (surface != EGL_NO_SURFACE) eglDestroySurface(display, surface)
        if (context != EGL_NO_CONTEXT) eglDestroyContext(display, context)
        eglTerminate(display)
        super.close()
    }

    fun pollEvents() {
        glfwPollEvents()
    }

    fun swapBuffers() {
        eglSwapBuffers(display, surface)
    }

    object Factory : Window.Factory {
        override fun create(name: String, handler: WindowHandler, width: Int, height: Int, isResizable: Boolean): Window {
            return GlfwGlesWindow(name, handler, width, height, isResizable)
        }
    }
}