package arc

import org.lwjgl.glfw.GLFW

/**
 * Utility class for executing some OS methods.
 */
internal object OS {

    /**
     * Execute terminal command.
     *
     * @param command Command for executing.
     *
     * @return Result of execution, true if alright, otherwise false.
     */
    @JvmStatic
    fun execSafe(vararg command: String): Boolean {
        try {
            Runtime.getRuntime().exec(command)
            return true
        } catch (t: Throwable) {
            return false
        }
    }

    /**
     * Get time by GLFW.
     */
    @JvmStatic
    fun getTime(): Long {
        return (GLFW.glfwGetTime() * 1000L).toLong()
    }

}