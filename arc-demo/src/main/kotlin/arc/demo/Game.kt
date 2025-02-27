package arc.demo

import arc.Application
import arc.Configuration
import arc.window.WindowHandler
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.opengl.GL15.*

/**
 * This own game main class.
 */
class Game : WindowHandler {

    private val application: Application = Application.find("opengl")

    fun start(configuration: Configuration = Configuration.create()) {
        application.init(configuration)

        // Set window handler to this instance.
        application.window.handler = this

        loop()
    }

    // Infinity game loop.
    private fun loop() {
        while (!GLFW.glfwWindowShouldClose(application.window.handle)) {
            glfwPollEvents()

            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            application.renderSystem.resetViewport()

            // Some render here.

            GLFW.glfwSwapBuffers(application.window.handle)
        }
    }

}