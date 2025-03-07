package arc.profiler.section

internal data class ArcActiveSection(
    override val name: String,
    override var startTime: Long = System.nanoTime(),
    override val child: MutableList<ActiveSection> = mutableListOf()
) : ActiveSection {

    override var result: SectionResult? = null
    private var endTime: Long = 0

    override fun start(name: String): ActiveSection {
        child.find { it.name == name }.takeIf { it != null }?.let {
            it.reset()

            return it
        }

        val section = ArcActiveSection(
            name = name,
        )

        child.add(section)

        return section
    }

    override fun end(): SectionResult {
        require(child.all { it.result != null }) { "Can not end this section! End it children before." }

        this.endTime = System.nanoTime()

        val result = ArcSectionResult(
            name = name,
            child = child.mapNotNull { it.result },
            startTime = startTime,
            endTime = endTime
        )

        this.result = result

        return result
    }

    override fun reset() {
        startTime = System.nanoTime()
        endTime = System.nanoTime()
        child.forEach { it.reset() }
        result = null
    }
}