package arc.profiler.section

internal class SimpleActiveSection(
    override val name: String,
    override var startTime: Long = System.nanoTime(),
    override val child: MutableList<ActiveSection> = mutableListOf()
) : ActiveSection {

    override var result: SimpleSectionResult? = null
    private var endTime: Long = 0

    override fun start(name: String): ActiveSection {
        child.find { it.name == name }.takeIf { it != null }?.let {
            it.reset()

            return it
        }

        val section = SimpleActiveSection(
            name = name,
        )

        child.add(section)

        return section
    }

    override fun end(): SectionResult {
        require(child.all { it.result != null }) { "Can not end this section! End it children before." }

        this.endTime = System.nanoTime()

        return this.result.also {
            it?.name = this.name
            it?.child = this.child.mapNotNull { it.result }
            it?.startTime = this.startTime
            it?.endTime = this.endTime
        } ?: run {
             this.result = SimpleSectionResult(
                name = name,
                child = child.mapNotNull { it.result },
                startTime = startTime,
                endTime = endTime
            )

            this.result!!
        }
    }

    override fun reset() {
        startTime = System.nanoTime()
        endTime = System.nanoTime()
        child.forEach { it.reset() }
        result = null
    }
}
