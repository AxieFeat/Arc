package arc.window

import org.lwjgl.glfw.GLFW

internal object GlfwWindowBackend : WindowBackend {

    override val name: String
        get() = "glfw"

    override val version: String
        get() {
            val major: IntArray = intArrayOf(1)
            val minor: IntArray = intArrayOf(1)
            val rev: IntArray = intArrayOf(1)

            GLFW.glfwGetVersion(major, minor, rev)

            return "${major.first()}.${minor.first()}.${rev.first()}"
        }
}
