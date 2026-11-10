package arc.files

import kotlinx.io.files.Path

/**
 * This interface represents a space to interact with and manage file locations for an application.
 * It provides utility methods for accessing files based on different location contexts.
 */
interface LocationSpace {

    /**
     * Directory of this application.
     */
    val applicationDirectory: Path

    /**
     * Get a file from a path.
     *
     * @param loc Type of file location.
     * @param path Path to file.
     *
     * @return New instance of [Path].
     */
    fun file(loc: FileLocation, path: String): Path

    /**
     * Get a file from the classpath.
     *
     * @param path Path to file.
     *
     * @return New instance of [Path].
     *
     * @see FileLocation.CLASSPATH
     */
    fun classpath(path: String): Path

    /**
     * Get a file from an absolute path.
     *
     * @param path Path to file.
     *
     * @return New instance of [Path].
     *
     * @see FileLocation.ABSOLUTE
     */
    fun absolute(path: String): Path

    /**
     * Get a file from directory of current application.
     *
     * @param path Path to file.
     *
     * @return New instance of [Path].
     *
     * @see FileLocation.LOCAL
     */
    fun local(path: String): Path
}
