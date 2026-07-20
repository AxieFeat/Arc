package arc.window.egl

import arc.window.Window
import arc.window.WindowBackend
import arc.window.WindowHandler
import arc.window.glfw.AbstractGlfwWindow
import io.github.oshai.kotlinlogging.KotlinLogging
import org.lwjgl.egl.EGL10.EGL_ALPHA_SIZE
import org.lwjgl.egl.EGL10.EGL_BLUE_SIZE
import org.lwjgl.egl.EGL10.EGL_DEPTH_SIZE
import org.lwjgl.egl.EGL10.EGL_FALSE
import org.lwjgl.egl.EGL10.EGL_GREEN_SIZE
import org.lwjgl.egl.EGL10.EGL_NONE
import org.lwjgl.egl.EGL10.EGL_NO_CONTEXT
import org.lwjgl.egl.EGL10.EGL_NO_SURFACE
import org.lwjgl.egl.EGL10.EGL_RED_SIZE
import org.lwjgl.egl.EGL10.EGL_STENCIL_SIZE
import org.lwjgl.egl.EGL10.EGL_SURFACE_TYPE
import org.lwjgl.egl.EGL10.EGL_TRUE
import org.lwjgl.egl.EGL10.EGL_WINDOW_BIT
import org.lwjgl.egl.EGL10.eglChooseConfig
import org.lwjgl.egl.EGL10.eglCreateContext
import org.lwjgl.egl.EGL10.eglCreateWindowSurface
import org.lwjgl.egl.EGL10.eglDestroyContext
import org.lwjgl.egl.EGL10.eglDestroySurface
import org.lwjgl.egl.EGL10.eglGetDisplay
import org.lwjgl.egl.EGL10.eglGetError
import org.lwjgl.egl.EGL10.eglInitialize
import org.lwjgl.egl.EGL10.eglMakeCurrent
import org.lwjgl.egl.EGL10.eglSwapBuffers
import org.lwjgl.egl.EGL10.eglTerminate
import org.lwjgl.egl.EGL11.eglSwapInterval
import org.lwjgl.egl.EGL12.EGL_RENDERABLE_TYPE
import org.lwjgl.egl.EGL13.EGL_CONTEXT_CLIENT_VERSION
import org.lwjgl.egl.EGL13.EGL_OPENGL_ES2_BIT
import org.lwjgl.egl.EGL14.EGL_DEFAULT_DISPLAY
import org.lwjgl.glfw.GLFW.GLFW_CLIENT_API
import org.lwjgl.glfw.GLFW.GLFW_NO_API
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.glfw.GLFWNativeCocoa.glfwGetCocoaWindow
import org.lwjgl.glfw.GLFWNativeWin32.glfwGetWin32Window
import org.lwjgl.glfw.GLFWNativeX11.glfwGetX11Window
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.Platform
import java.nio.IntBuffer

class EglGlesWindow(
    name: String,
    handler: WindowHandler,
    width: Int,
    height: Int,
    isResizable: Boolean
) : AbstractGlfwWindow(name, handler, width, height, isResizable) {

    override val logger = KotlinLogging.logger {}

    override val backend: WindowBackend = EglWindowBackend

    private val display: Long = eglGetDisplay(EGL_DEFAULT_DISPLAY)
    private var surface: Long = -1
    private var context: Long = -1
    private var config: Long = -1

    override var isVsync: Boolean = true
        set(value) {
            if(value && !field) {
                eglSwapInterval(display, EGL_TRUE)
            } else if(!value && field) {
                eglSwapInterval(display, EGL_FALSE)
            }

            field = value
        }

    init {
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API)
    }

    override fun create() {
        super.create()

        MemoryStack.stackPush().use { stack ->
            val major = stack.mallocInt(1)
            val minor = stack.mallocInt(1)

            require(eglInitialize(display, major, minor)) { "EGL init failed" }

            logger.debug { "Created EGL display. EGL version: ${major[0]}.${minor[0]}" }

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
                Platform.MACOSX -> glfwGetCocoaWindow(handle)
                Platform.WINDOWS -> glfwGetWin32Window(handle)
                Platform.LINUX -> glfwGetX11Window(handle)
                else -> error("Unsupported platform")
            }

            surface = eglCreateWindowSurface(display, config, NativeEGLBridge.getNativeLayer(nativeWindow), null as IntBuffer?)

            require(surface != EGL_NO_SURFACE) {
                "eglCreateWindowSurface failed: 0x${eglGetError().toString(16)}"
            }

            require(eglMakeCurrent(display, surface, surface, context)) {
                "eglMakeCurrent failed: ${eglGetError()}"
            }
        }
        logger.debug { "Created EGL surface ${surface.toString(16)} via GLFW" }
    }

    override fun close() {
        logger.info { "Closing EGL window" }

        eglMakeCurrent(display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT)
        if (surface != EGL_NO_SURFACE) eglDestroySurface(display, surface)
        if (context != EGL_NO_CONTEXT) eglDestroyContext(display, context)
        eglTerminate(display)

        logger.debug { "Destroyed EGL surface $surface" }
        logger.debug { "Destroyed EGL context $context" }
        logger.info { "EGL window has been closed" }

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
            return EglGlesWindow(name, handler, width, height, isResizable)
        }
    }
}
