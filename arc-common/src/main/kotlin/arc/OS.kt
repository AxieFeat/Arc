package arc

import org.lwjgl.glfw.GLFW

internal object OS {

    @JvmStatic
    fun execSafe(vararg command: String): Boolean {
        try {
            Runtime.getRuntime().exec(command)
            return true
        } catch (t: Throwable) {
            return false
        }
    }

    @JvmStatic
    fun getTime(): Long {
        return (GLFW.glfwGetTime() * 1000L).toLong()
    }

}