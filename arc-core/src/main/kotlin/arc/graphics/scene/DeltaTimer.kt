package arc.graphics.scene

import arc.window.Window

internal class DeltaTimer(
    val window: Window
) {

    private var lastTime: Long = TimeUtil.getTime(window)

    var deltaTime: Float = 0f
        private set

    @Suppress("MagicNumber") // TODO What is 1000L mean here?
    fun update() {
        val currentTime = TimeUtil.getTime(window)
        deltaTime = (currentTime - lastTime) / 1000f
        lastTime = currentTime
    }
}
