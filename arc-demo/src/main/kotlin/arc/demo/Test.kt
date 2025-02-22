import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.*
import java.nio.file.Files
import java.nio.file.Paths

class ModelRenderer {
    private var window: Long = 0
    private var vao = 0
    private var vbo = 0
    private val textureID = 0

    private class Model(
        var elements: List<Element>
    )

    private class Element(
        var from: IntArray = intArrayOf(),
        var to: IntArray = intArrayOf()
    )

    fun run() {
        init()
        loop()
        GLFW.glfwDestroyWindow(window)
        GLFW.glfwTerminate()
    }

    private fun init() {
        check(GLFW.glfwInit()) { "Unable to initialize GLFW" }
        window = GLFW.glfwCreateWindow(800, 600, "3D Model Renderer", 0, 0)
        val vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())
        GLFW.glfwSetWindowPos(window, vidMode!!.width() / 2 - 400, vidMode.height() / 2 - 300)
        GLFW.glfwMakeContextCurrent(window)
        GL.createCapabilities()
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        loadModel()
    }

    private fun loadModel() {
        try {
            val model = Model(
                elements = listOf(
                    Element(
                        from = intArrayOf(
                            5,
                            0,
                            7
                        ),
                        to = intArrayOf(
                            11,
                            2,
                            9
                        )
                    ),
                    Element(
                        from = intArrayOf(
                            7,
                            2,
                            7
                        ),
                        to = intArrayOf(
                            9,
                            8,
                            9
                        )
                    )
                )
            )

            val vertices = BufferUtils.createFloatBuffer(model.elements!!.size * 6 * 6)
            for (element in model.elements!!) {
                val x1 = element.from[0].toFloat()
                val y1 = element.from[1].toFloat()
                val z1 = element.from[2].toFloat()
                val x2 = element.to[0].toFloat()
                val y2 = element.to[1].toFloat()
                val z2 = element.to[2].toFloat()
                val cubeVertices = floatArrayOf(
                    x1, y1, z1, x2, y1, z1, x2, y2, z1,
                    x1, y1, z1, x2, y2, z1, x1, y2, z1,
                    x1, y1, z2, x2, y1, z2, x2, y2, z2,
                    x1, y1, z2, x2, y2, z2, x1, y2, z2
                )
                vertices.put(cubeVertices)
            }
            vertices.flip()

            vao = GL30.glGenVertexArrays()
            vbo = GL15.glGenBuffers()
            GL30.glBindVertexArray(vao)
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0)
            GL20.glEnableVertexAttribArray(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
            GL30.glBindVertexArray(vao)
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 36)
            GLFW.glfwSwapBuffers(window)
            GLFW.glfwPollEvents()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ModelRenderer().run()
        }
    }
}