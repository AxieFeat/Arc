package arc.demo.screen

import arc.demo.VoxelGame
import arc.graphics.AbstractScene

abstract class Screen(
    val id: String
) : AbstractScene(VoxelGame.application) {

    private val child = mutableSetOf<Screen>()

    protected var name: String
        get() = application.window.name
        set(value) {
            application.window.name = value
        }

    protected fun addChild(screen: Screen) {
        child.add(screen)
    }

    protected fun removeChild(screen: Screen) {
        child.remove(screen)
    }

    override fun render() {
        if (isSkipRender) return
        updateDelta()
        camera.updateAspect(application.window.width, application.window.height)

        doRender()
        child.forEach { it.render() }

        calculateFps()
    }

    abstract fun doRender()

}