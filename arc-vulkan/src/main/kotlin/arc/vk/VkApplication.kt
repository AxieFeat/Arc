package arc.vk

import arc.AbstractApplication
import arc.Application
import arc.ApplicationBackend
import arc.graphics.RenderSystem
import arc.window.Window
import java.io.File

internal object VkApplication : AbstractApplication() {

    override val backend: ApplicationBackend = VkApplicationBackend

    override lateinit var window: Window
    override lateinit var renderSystem: RenderSystem

    override fun init() {

    }

    override fun screenshot(folder: File, name: String) {

    }

    override fun close() {
        window.close()
    }

    internal object Factory : Application.Factory {
        override fun create(): Application {
            return VkApplication
        }
    }

}