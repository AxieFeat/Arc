package arc.graphics.scene

import arc.Application
import arc.graphics.Camera

/**
 * This abstract class represents minimal scene implementation.
 *
 * @param application Application instance.
 */
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

        while (TimeUtil.getTime(application.window) >= this.debugUpdateTime + FPS_UPDATE_INTERVAL) {
            this.fps = this.fpsCounter
            this.debugUpdateTime += FPS_UPDATE_INTERVAL
            this.fpsCounter = 0
            onFpsUpdate(fps)
        }
    }

    protected fun updateTimer() {
        timer.update()
        this.delta = timer.deltaTime
    }

    /**
     * This method is called when fps is updated. Fps updates every second.
     */
    open fun onFpsUpdate(fps: Int) {}

    companion object {

        /**
         * Interval in milliseconds to update fps value.
         */
        const val FPS_UPDATE_INTERVAL = 1000L
    }
}
