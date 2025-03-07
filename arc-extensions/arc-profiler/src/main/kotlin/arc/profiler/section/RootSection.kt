package arc.profiler.section

/**
 * This interface represents general section of profiler.
 */
interface RootSection : ActiveSection {

    /**
     * Last result of this section. If null - result not found.
     */
    override val result: TreeSectionResult?

    /**
     * End this section. Before calling end all children.
     *
     * @return Instance of [TreeSectionResult].
     */
    override fun end(): TreeSectionResult

}