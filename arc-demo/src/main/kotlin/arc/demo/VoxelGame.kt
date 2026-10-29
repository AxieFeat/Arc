package arc.demo

import arc.Application
import arc.audio.SoundEngine
import arc.demo.screen.FaultyTerminalScreen
import arc.demo.screen.FractalScreen
import arc.demo.screen.Screen
import arc.demo.screen.TerrainScreen
import arc.demo.shader.ShaderContainer
import arc.graphics.vertex.VertexFormat
import arc.input.GlfwInputEngine
import arc.window.WindowHandler
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.system.exitProcess

object VoxelGame : WindowHandler {

    val application: Application = Application.find()
    private val soundEngine: SoundEngine = SoundEngine.find()
    var screen: Screen? = null

    private val logger = KotlinLogging.logger {}

    fun start() {
        application.init()
        soundEngine.start()

        GlfwInputEngine.hook(application.window)

        loadShaders()

        // Set window handler to this instance.
        application.window.handler = this

        // Demo with julia fractal.
        screen = TerrainScreen

        // Demo with minecraft-like game.
//        setScreen(FaultyTerminalScreen)

//        val asset = classpath("arc/sound/pigstep.ogg").asFileAsset()
//        val sound = SoundLoader.of(SoundFormat.OGG).load(asset)
//
//        sound.play(volume = 0.3f, loop = true)

        loop()

        // Close application after exit from loop.
        soundEngine.stop()
        application.close()

        exitProcess(0)
    }

    // Infinity game loop.
    private fun loop() {
        val renderSystem = application.renderContext

        while (!application.window.shouldClose()) {
            renderSystem.beginFrame()

            screen?.render() // Render our scene.

            renderSystem.endFrame()
        }
    }

    private fun loadShaders() {
        logger.info { "Loading shaders..." }

        val start = System.currentTimeMillis()
        ShaderContainer
        VertexFormat

        logger.info { "Shaders loaded in ${System.currentTimeMillis() - start} ms" }
    }

}
