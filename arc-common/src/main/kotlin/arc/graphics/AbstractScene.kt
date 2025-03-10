package arc.graphics

import arc.Application
import arc.OS
import arc.util.Timer
import org.lwjgl.glfw.GLFW

@Suppress("LeakingThis")
abstract class AbstractScene(
    private val application: Application,
    tps: Float,
) : Scene {

    private val timer = Timer(tps)
    private var fpsCounter = 0
    private var debugUpdateTime: Long = OS.getTime()

    override val drawer: Drawer = application.renderSystem.drawer
    override var fps: Int = 0
    override var delta: Float = 0f
    override val inUse: Boolean = application.renderSystem.scene == this

    override var isSkipRender: Boolean = false

    override var showCursor: Boolean = true
        set(value) {
            field = value

            GLFW.glfwSetInputMode(
                application.window.handle,
                GLFW.GLFW_CURSOR,
                if (field) GLFW.GLFW_CURSOR_NORMAL else GLFW.GLFW_CURSOR_HIDDEN
            )
        }

    protected fun calculateFps() {
        fpsCounter++

        while (OS.getTime() >= this.debugUpdateTime + 1000L) {
            this.fps = this.fpsCounter
            this.debugUpdateTime += 1000L
            this.fpsCounter = 0
        }
    }

    protected fun updateDelta() {
        timer.updateTimer()
        this.delta = timer.renderPartialTicks
    }

}