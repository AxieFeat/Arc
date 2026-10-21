package arc.demo

import arc.Application
import arc.audio.SoundEngine
import arc.demo.screen.FaultyTerminalScreen
import arc.demo.screen.FractalScreen
import arc.demo.screen.Screen
import arc.demo.shader.ShaderContainer
import arc.graphics.vertex.VertexFormat
import arc.input.GlfwInputEngine
import arc.window.WindowHandler
import kotlin.system.exitProcess

object VoxelGame : WindowHandler {

    val application: Application = Application.find()
    private val soundEngine: SoundEngine = SoundEngine.find()
    var screen: Screen? = null

    fun start() {
        application.init()
        soundEngine.start()

        GlfwInputEngine.hook(application.window)

//        loadShaders()

        // Set window handler to this instance.
        application.window.handler = this

        // Demo with julia fractal.
        screen = FaultyTerminalScreen

        // Demo with minecraft-like game.
//        setScreen(FaultyTerminalScreen)

        application.window.isVsync = false

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
        application.window.name = "Loading shaders..."

        val start = System.currentTimeMillis()
        ShaderContainer
        VertexFormat

        application.window.name = "Shaders loaded in ${System.currentTimeMillis() - start} ms"
    }

}
