package arc.profiler.section

internal class SimpleSectionResult(
    override var name: String,
    override var child: List<SectionResult>,
    override var startTime: Long,
    override var endTime: Long = System.nanoTime()
) : SectionResult {

    override val duration: Long = endTime - startTime
}
