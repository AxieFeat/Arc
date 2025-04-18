package arc.demo

import arc.Application
import arc.Configuration
import arc.audio.SoundEngine
import arc.demo.screen.TerrainScreen
import arc.demo.screen.Screen
import arc.demo.shader.ShaderContainer
import arc.graphics.vertex.VertexFormat
import arc.window.WindowHandler
import org.lwjgl.opengl.GL11.GL_RENDERER
import org.lwjgl.opengl.GL11.GL_VENDOR
import org.lwjgl.opengl.GL11.glGetString

object VoxelGame : WindowHandler {

    val application: Application = Application.find()
    private val soundEngine: SoundEngine = SoundEngine.find()

    fun start(configuration: Configuration = Configuration.create()) {
        application.init(configuration)
        soundEngine.start()

        loadShaders()

        // Set window handler to this instance.
        application.window.handler = this
        application.window.isVsync = true
        setScreen(TerrainScreen)

//        val asset = classpath("arc/sound/pigstep.ogg").asFileAsset()
//        val sound = SoundLoader.of(SoundFormat.OGG).load(asset)
//
//        sound.play(volume = 0.3f, loop = true)

        if(application.platform.isIGpu) {
            println("Warning! Engine started at iGPU.")
        }

        loop()

        // Close application after exit from loop.
        soundEngine.stop()
        application.close()
    }

    fun setScreen(screen: Screen) {
        application.renderSystem.setScene(screen)
    }

    // Infinity game loop.
    private fun loop() {
        val renderSystem = application.renderSystem

        while (!application.window.shouldClose()) {
            renderSystem.beginFrame()

            renderSystem.scene.render() // Render our scene.

            renderSystem.endFrame()
        }
    }

    private fun loadShaders() {
        application.window.name = "Loading shaders..."

        val start = System.currentTimeMillis()
        ShaderContainer
        VertexFormat

        application.window.name = "Shaders loaded in ${System.currentTimeMillis() - start} ms"
    }

}