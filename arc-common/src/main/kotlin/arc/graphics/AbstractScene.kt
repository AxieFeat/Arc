package arc.graphics

import arc.Application
import arc.OS
import arc.util.Timer
import org.lwjgl.glfw.GLFW

abstract class AbstractScene(
    val application: Application,
) : Scene {

    private val timer = Timer()
    private var fpsCounter = 0
    private var debugUpdateTime: Long = OS.getTime()

    override val drawer: Drawer = application.renderSystem.drawer
    override val camera: Camera = Camera.create(45f, application.window.width.toFloat(), application.window.height.toFloat())
    override var fps: Int = 0
    override var delta: Float = 0f
    override val isUse: Boolean
        get() = application.renderSystem.scene == this

    override var isSkipRender: Boolean = false

    override var isShowCursor: Boolean = true
        set(value) {
            field = value

            GLFW.glfwSetInputMode(
                application.window.handle,
                GLFW.GLFW_CURSOR,
                if (field) GLFW.GLFW_CURSOR_NORMAL else GLFW.GLFW_CURSOR_DISABLED
            )
        }

    protected open fun calculateFps() {
        fpsCounter++

        while (OS.getTime() >= this.debugUpdateTime + 1000L) {
            this.fps = this.fpsCounter
            this.debugUpdateTime += 1000L
            this.fpsCounter = 0
            onFpsUpdate(fps)
        }
    }

    protected fun updateTimer() {
        timer.update()
        this.delta = timer.deltaTime
    }

    open fun onFpsUpdate(fps: Int) {}

}