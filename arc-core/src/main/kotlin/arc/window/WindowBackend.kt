package arc.window

/**
 * This interface represents info about window backend implementation.
 */
interface WindowBackend {

    /**
     * It should be a name of implementation in the lower case without spaces or any special symbol.
     */
    val name: String

    /**
     * Version of a backend library.
     */
    val version: String

}