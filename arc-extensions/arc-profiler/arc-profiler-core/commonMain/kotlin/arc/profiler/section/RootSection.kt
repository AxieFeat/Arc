package arc.profiler.section

import arc.annotations.ImmutableType

/**
 * This interface represents general section of profiler.
 */
@ImmutableType
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
