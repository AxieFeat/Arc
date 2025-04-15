package arc.profiler.section

import arc.annotations.ImmutableType
import kotlin.jvm.Throws

/**
 * This interface represents not completed section of profiler.
 */
@ImmutableType
interface ActiveSection : Section {

    /**
     * Last result of this section. If null - result not found.
     */
    val result: SectionResult?

    /**
     * Children of this section.
     */
    override val child: List<ActiveSection>

    /**
     * Add child for this section.
     *
     * @param name Name of section.
     *
     * @return Instance of [ActiveSection].
     */
    fun start(name: String): ActiveSection

    /**
     * End this section. Before calling end all children.
     *
     * @return Instance of [SectionResult].
     */
    @Throws(IllegalArgumentException::class)
    fun end(): SectionResult

    /**
     * Reset this section.
     */
    fun reset()

}