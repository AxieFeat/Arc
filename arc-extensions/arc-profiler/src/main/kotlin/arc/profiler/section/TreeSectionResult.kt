package arc.profiler.section

import arc.annotations.ImmutableType

@ImmutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface TreeSectionResult : SectionResult {

    /**
     * Children of this section.
     */
    override val child: List<TreeSectionResult>

    /**
     * Root section. If null - this section already root.
     */
    val root: TreeSectionResult?

    /**
     * How many percentages this section took when the parent section was ended.
     * This is the percentage of time compared to the other sections of the parent.
     */
    @get:JvmName("usage")
    val usage: Double

    /**
     * Create pretty string of this section.
     *
     * @param level Level of recurse (Not use).
     *
     * @return Pretty string for debug in example.
     */
    fun pretty(level: Int = 0): String

}