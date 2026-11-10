package arc.files

import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import java.nio.file.Paths

internal object SimpleLocationSpace : LocationSpace {

    override val applicationDirectory: Path = run {
        val file = Path(Paths.get(
            SimpleLocationSpace::class.java.protectionDomain.codeSource.location.toURI()
        ).parent.toString())

        if (!SystemFileSystem.exists(file)) {
            SystemFileSystem.createDirectories(file)
        }

        return@run file
    }

    override fun file(loc: FileLocation, path: String): Path {
        return when (loc) {
            FileLocation.CLASSPATH -> classpath(path)
            FileLocation.ABSOLUTE -> absolute(path)
            FileLocation.LOCAL -> local(path)
        }
    }

    override fun classpath(path: String): Path {
        val classLoader = Thread.currentThread().contextClassLoader
        val resourceUrl = classLoader.getResource(path) ?: return Path(".")

        return Path(Paths.get(resourceUrl.toURI()).toString())
    }

    override fun absolute(path: String): Path {
        return Path(path)
    }

    override fun local(path: String): Path {
        return Path(applicationDirectory, path)
    }
}
