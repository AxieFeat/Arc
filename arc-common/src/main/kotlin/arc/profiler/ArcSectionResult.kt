package arc.profiler

internal data class ArcSectionResult(
    override val name: String,
    override val time: Long,
) : SectionResult {

    override fun compareTo(other: SectionResult): Int {
        return other.name.compareTo(this.name)
    }

}