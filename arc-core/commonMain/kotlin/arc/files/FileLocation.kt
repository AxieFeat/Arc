package arc.files

/**
 * Enum representing different types of file locations that can be specified
 * within an application. This is used to determine where and how a file
 * should be accessed or loaded.
 */
enum class FileLocation {

    /**
     * Location in application classpath, that is, inside the executable jar file.
     */
    CLASSPATH,

    /**
     * Absolute path to file.
     *
     * This means that the path to the file will be specified in full.
     */
    ABSOLUTE,

    /**
     * Local path to file.
     *
     * This means that the path to the file will be specified starting
     * from the directory where the Application is running.
     */
    LOCAL
}
