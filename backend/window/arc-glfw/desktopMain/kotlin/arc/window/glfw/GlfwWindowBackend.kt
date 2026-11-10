package arc.window.glfw

import arc.window.WindowBackend
import org.lwjgl.glfw.GLFW

internal object GlfwWindowBackend : WindowBackend {

    override val name: String = "glfw"

    override val version: String = run {
        val major: IntArray = intArrayOf(1)
        val minor: IntArray = intArrayOf(1)
        val rev: IntArray = intArrayOf(1)

        GLFW.glfwGetVersion(major, minor, rev)

        return@run "${major.first()}.${minor.first()}.${rev.first()}"
    }
}
