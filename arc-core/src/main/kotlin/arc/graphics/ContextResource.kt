package arc.graphics

/**
 * Generation of the creating native context. Zero denotes an implementation without generation tracking.
 */
interface ContextResource {

    /**
     * Identity of the creating context; zero means generation tracking is unavailable.
     */
    val resourceGeneration: Long get() = 0L
}
