package arc.util

import arc.OS

internal class Timer {

    private var lastTime: Long = OS.getTime()
    var deltaTime: Float = 0f

    fun update() {
        val currentTime = OS.getTime()
        deltaTime = (currentTime - lastTime) / 1000f
        lastTime = currentTime
    }

}