package arc.graphics.scene

import arc.Application
import arc.graphics.Camera

abstract class AbstractScene(
    val application: Application,
) : Scene {

    private val timer = DeltaTimer(application.window)
    private var fpsCounter = 0
    private var debugUpdateTime: Long = TimeUtil.getTime(application.window)

    override val camera: Camera = Camera.of(45f, application.window.width.toFloat(), application.window.height.toFloat())
    override var fps: Int = 0
    override var delta: Float = 0f

    override var isShowCursor: Boolean
        get() = application.window.isShowCursor
        set(value) {
            application.window.isShowCursor = value
        }

    protected open fun calculateFps() {
        fpsCounter++

        while (TimeUtil.getTime(application.window) >= this.debugUpdateTime + 1000L) {
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