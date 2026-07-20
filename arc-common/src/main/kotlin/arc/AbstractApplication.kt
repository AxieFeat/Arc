package arc

import arc.OS.execSafe
import arc.files.SimpleLocationSpace
import arc.files.LocationSpace
import io.github.oshai.kotlinlogging.KLogger
import java.io.File
import kotlin.time.Clock
import kotlin.time.Instant

abstract class AbstractApplication : Application {

    override val locationSpace: LocationSpace = SimpleLocationSpace
    protected abstract val logger: KLogger
    private var initTime: Instant? = null

    override fun openURL(url: String) {
        Thread {
            val result = when (backend.device.os) {
                OSPlatform.MACOS -> execSafe("open", url)
                OSPlatform.LINUX -> execSafe("xdg-open", url)
                OSPlatform.WINDOWS -> execSafe("rundll32", "url.dll,FileProtocolHandler", url)

                else -> throw UnsupportedOperationException("Can not open URL on this platform.")
            }

            if(!result) throw UnsupportedOperationException("Occurred error in the opening URL.")
        }.also { it.isDaemon = true }.start()
    }

    override fun openFolder(folder: File) {
        require(folder.isDirectory) { "File is not a directory." }

        Thread {
            val result = when (backend.device.os) {
                OSPlatform.MACOS -> execSafe("open", folder.absolutePath)
                OSPlatform.LINUX -> execSafe("xdg-open", folder.absolutePath)
                OSPlatform.WINDOWS -> execSafe("explorer.exe /select," + folder.absolutePath.replace("/", "\\"))

                else -> throw UnsupportedOperationException("Can not open folder on this platform.")
            }

            if(!result) throw UnsupportedOperationException("Occurred error in the opening folder.")
        }.also { it.isDaemon = true }.start()
    }

    override fun init() {
        initTime = Clock.System.now()
        logger.info { "Initializing application..." }
        logger.info { "Running with Java ${backend.device.java}" }
    }

    protected fun printStartedMessage() {
        logger.info { "Application started" }
        logger.info { "Selected platform: ${backend.device.os}" }
        logger.info { "CPU | GPU: [ ${backend.device.cpu} | ${backend.device.gpu} ]" }
        logger.info { "Render backend: ${backend.name.uppercase()} [${backend.version}]" }
        logger.info { "Window backend: ${window.backend.name.uppercase()} [${window.backend.version}]" }
        initTime?.let { time ->
            logger.info { "Started in ${Clock.System.now().minus(time).inWholeMilliseconds} ms" }
        }
    }
}
