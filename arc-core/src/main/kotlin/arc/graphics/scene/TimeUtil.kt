package arc.graphics.scene

import arc.window.Window

internal object TimeUtil {

    @JvmStatic
    fun getTime(window: Window): Long {
        return (window.timeFromInitialize * 1000L).toLong()
    }

}