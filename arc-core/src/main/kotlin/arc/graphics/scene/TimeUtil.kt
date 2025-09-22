package arc.graphics.scene

import arc.window.Window

internal object TimeUtil {

    @JvmStatic
    @Suppress("MagicNumber") // TODO What is 1000L mean here?
    fun getTime(window: Window): Long {
        return (window.timeFromInitialize * 1000L).toLong()
    }
}
