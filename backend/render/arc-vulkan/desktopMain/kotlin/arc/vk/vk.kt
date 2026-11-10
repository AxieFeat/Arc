package arc.vk

import arc.Application
import arc.ArcObjectProvider

fun main() {
    ArcObjectProvider.install()
    ArcObjectProvider.bootstrap()

    Vulkan.preload()

    val application = Application.find()

    application.init()

    while (!application.window.shouldClose()) {

    }
}