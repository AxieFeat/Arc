package arc.demo.screen

import arc.demo.VoxelGame
import arc.graphics.AbstractScene
import kotlin.math.roundToInt

abstract class Screen(
    val id: String
) : AbstractScene(VoxelGame.application) {

    private val child = mutableSetOf<Screen>()

    protected var name: String
        get() = application.window.name
        set(value) {
            application.window.name = value
        }

    protected var frameTime: Double = 0.0
        private set

    protected fun addChild(screen: Screen) {
        child.add(screen)
    }

    protected fun removeChild(screen: Screen) {
        child.remove(screen)
    }

    override fun render() {
        if (isSkipRender) return
        val start = System.nanoTime()
        updateTimer()
        camera.updateAspect(application.window.width, application.window.height)

        doRender()
        child.forEach { it.render() }

        calculateFps()
        frameTime = ((System.nanoTime() - start).toDouble() / 1000000).round()
    }

    abstract fun doRender()

    private fun Double.round(): Double {
        return (this * 100).roundToInt() / 100.0
    }

}