package arc.gles.window

import arc.window.AbstractGlfwWindow
import arc.window.Window
import arc.window.WindowHandler
import org.lwjgl.egl.EGL10.*
import org.lwjgl.egl.EGL12.EGL_RENDERABLE_TYPE
import org.lwjgl.egl.EGL13.EGL_CONTEXT_CLIENT_VERSION
import org.lwjgl.egl.EGL13.EGL_OPENGL_ES2_BIT
import org.lwjgl.egl.EGL14.EGL_DEFAULT_DISPLAY
import org.lwjgl.egl.EGL15.eglGetPlatformDisplay
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWNativeCocoa.glfwGetCocoaView
import org.lwjgl.glfw.GLFWNativeCocoa.glfwGetCocoaWindow
import org.lwjgl.glfw.GLFWNativeEGL.glfwGetEGLContext
import org.lwjgl.glfw.GLFWNativeEGL.glfwGetEGLDisplay
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

        glfwShowWindow(handle)

        MemoryStack.stackPush().use { stack ->
            if (display == EGL_NO_DISPLAY) error("No EGL display")

            val major = stack.mallocInt(1)
            val minor = stack.mallocInt(1)
            if (!eglInitialize(display, major, minor)) error("EGL init failed")

            val eglAttribs = listOf(
                EGL_RED_SIZE to 8,
                EGL_GREEN_SIZE to 8,
                EGL_BLUE_SIZE to 8,
                EGL_ALPHA_SIZE to 8,
                EGL_DEPTH_SIZE to 24,
                EGL_STENCIL_SIZE to 8,
                EGL_RENDERABLE_TYPE to EGL_OPENGL_ES2_BIT,
                EGL_SURFACE_TYPE to EGL_WINDOW_BIT
            )

            val attribs = stack.mallocInt(eglAttribs.size * 2 + 1)
            for ((key, value) in eglAttribs) {
                attribs.put(key)
                attribs.put(value)
            }
            attribs.put(EGL_NONE)
            attribs.flip()

            val numConfigs = stack.mallocInt(1)
            val configs = stack.mallocPointer(1)

            require(eglChooseConfig(display, attribs, configs, numConfigs)) {
                "eglChooseConfig failed: ${eglGetError()}"
            }

            if (numConfigs[0] == 0) {
                error("No suitable EGL config found for ANGLE")
            }

            config = configs[0]
            if (config == 0L) error("No suitable EGL config found")

            val ctxAttribs = stack.mallocInt(3).apply {
                put(EGL_CONTEXT_CLIENT_VERSION)
                put(2)
                put(EGL_NONE)
                flip()
            }

            context = eglCreateContext(display, config, EGL_NO_CONTEXT, ctxAttribs)
            if (context == EGL_NO_CONTEXT) error("eglCreateContext failed: ${eglGetError()}")

            val nativeWindow = when(Platform.get()) {
                Platform.MACOSX -> glfwGetCocoaView(handle)
                Platform.WINDOWS -> glfwGetWin32Window(handle)
                Platform.LINUX -> glfwGetX11Window(handle)
                else -> error("Unsupported platform")
            }

            surface = eglCreateWindowSurface(display, config, nativeWindow, null as IntBuffer?)

            if (surface == EGL_NO_SURFACE) {
                val error = eglGetError()
                error("eglCreateWindowSurface failed: 0x${error.toString(16)}")
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