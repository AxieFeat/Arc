package arc.files

import arc.annotations.ImmutableType
import java.io.File

/**
 * This interface represents a space to interact with and manage file locations for an application.
 * It provides utility methods for accessing files based on different location contexts.
 */
@ImmutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface LocationSpace {

    /**
     * Directory of this application.
     */
    @get:JvmName("applicationDirectory")
    val applicationDirectory: File

    /**
     * Get file from path.
     *
     * @param loc Type of file location.
     * @param path Path to file.
     *
     * @return New instance of [File].
     */
    fun file(loc: FileLocation, path: String): File

    /**
     * Get file from classpath.
     *
     * @param path Path to file.
     *
     * @return New instance of [File].
     *
     * @see FileLocation.CLASSPATH
     */
    fun classpath(path: String): File

    /**
     * Get file from absolute path.
     *
     * @param path Path to file.
     *
     * @return New instance of [File].
     *
     * @see FileLocation.ABSOLUTE
     */
    fun absolute(path: String): File

    /**
     * Get file from directory of current application.
     *
     * @param path Path to file.
     *
     * @return New instance of [File].
     *
     * @see FileLocation.LOCAL
     */
    fun local(path: String): File

}