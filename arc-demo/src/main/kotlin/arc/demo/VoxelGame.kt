package arc.demo

import arc.Application
import arc.audio.SoundEngine
import arc.demo.screen.FractalScreen
import arc.demo.screen.Screen
import arc.demo.screen.TerrainScreen
import arc.demo.shader.ShaderContainer
import arc.graphics.vertex.VertexFormat
import arc.input.GlfwInputEngine
import arc.window.WindowHandler
import kotlin.system.exitProcess

object VoxelGame : WindowHandler {

    val application: Application = Application.find()
    private val soundEngine: SoundEngine = SoundEngine.find()

    fun start() {
        application.init()
        soundEngine.start()

        GlfwInputEngine.hook(application.window)

        loadShaders()

        // Set window handler to this instance.
        application.window.handler = this

        setScreen(TerrainScreen)

//        val asset = classpath("arc/sound/pigstep.ogg").asFileAsset()
//        val sound = SoundLoader.of(SoundFormat.OGG).load(asset)
//
//        sound.play(volume = 0.3f, loop = true)

        println("Running with Java ${application.backend.device.java}")
        println("=".repeat(30))
        println("Selected platform: ${application.backend.device.os}")
        println("CPU | GPU: [ ${application.backend.device.cpu.name} | ${application.backend.device.gpu.firstOrNull()?.name ?: "N/A"} ]")
        println("Backend: ${application.backend.name.uppercase()} [${application.backend.version}]")
        println("Window backend: ${application.window.backend.name.uppercase()} [${application.window.backend.version}]")
        println("=".repeat(30))

        if(application.backend.isIGpu) {
            println("Warning! Engine started at iGPU.")
        }

        loop()

        // Close application after exit from loop.
        soundEngine.stop()
        application.close()

        exitProcess(0)
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