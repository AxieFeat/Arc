package arc.demo

import arc.Application
import arc.Configuration
import arc.demo.world.World
import arc.window.WindowHandler
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.opengl.*
import org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray
import org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer


/**
 * This own game main class.
 */
class Game : WindowHandler {

    private var vao = 0
    private var vbo = 0

    private val application: Application = Application.find("opengl")
    private val world = World()

    fun start(configuration: Configuration = Configuration.create()) {
        application.init(configuration)

        // Set window handler to this instance.
        application.window.handler = this

        // Get central chunk from world.
        //val centralChunk = world[0, 0]

        // Add entity to this chunk.
//        centralChunk.add(
//            entity = Entity.create(
//                id = 0,
//                chunk = centralChunk,
//                model = Model.create(
//                    asset = ModelAsset.from(classpath("arc/model/cube.obj"))
//                ).also {
//                    // Update position of model.
//                    it.position = Point3d.of(0.0, 1.0, 0.0)
//                }
//            )
//        )

        loadModel()
        loop()
    }

    private class Model(
        var elements: List<Element>
    )

    private class Element(
        var from: IntArray = intArrayOf(),
        var to: IntArray = intArrayOf()
    )

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

            val vertices = BufferUtils.createFloatBuffer(model.elements.size * 6 * 6)
            for (element in model.elements) {
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

            vao = glGenVertexArrays()
            vbo = glGenBuffers()
            glBindVertexArray(vao)
            glBindBuffer(GL_ARRAY_BUFFER, vbo)
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
            glEnableVertexAttribArray(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Infinity game loop.
    private fun loop() {
        while (!GLFW.glfwWindowShouldClose(application.window.handle)) {
            glfwPollEvents()

            GL41.glClear(GL41.GL_COLOR_BUFFER_BIT or GL41.GL_DEPTH_BUFFER_BIT)
            application.renderSystem.resetViewport()

            GL30.glBindVertexArray(vao)
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 36)

            GLFW.glfwSwapBuffers(application.window.handle)
        }
    }

}