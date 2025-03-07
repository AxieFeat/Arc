package arc.profiler.section

internal data class ArcSectionResult(
    override val name: String,
    override val child: List<SectionResult>,
    override val startTime: Long,
    override val endTime: Long = System.nanoTime()
) : SectionResult {

    override val duration: Long = endTime - startTime

}