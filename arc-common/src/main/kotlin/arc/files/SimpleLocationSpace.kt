package arc.files

import java.io.File

internal object SimpleLocationSpace : LocationSpace {

    override val applicationDirectory: File = run {
        val file = File(
            SimpleLocationSpace::class.java.getProtectionDomain().codeSource.location.toURI()
        ).parentFile

        if (!file.exists()) {
            file.mkdirs()
        }

        return@run file
    }

    override fun file(loc: FileLocation, path: String): File {
        return when (loc) {
            FileLocation.CLASSPATH -> classpath(path)
            FileLocation.ABSOLUTE -> absolute(path)
            FileLocation.LOCAL -> local(path)
        }
    }

    override fun classpath(path: String): File {
        val classLoader = Thread.currentThread().contextClassLoader
        val resourceUrl = classLoader.getResource(path) ?: return File("")

        return File(resourceUrl.toURI())
    }

    override fun absolute(path: String): File {
        return File(path)
    }

    override fun local(path: String): File {
        return File(applicationDirectory, path)
    }

}