package arc.graphics

/** Generation of the creating native context. Zero denotes an implementation without generation tracking. */
interface ContextResource {

    val resourceGeneration: Long get() = 0L
}
