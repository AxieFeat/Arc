package arc.profiler.section

internal class SimpleRootSection(
    override val name: String = "root",
    override var startTime: Long = System.nanoTime(),
    override val child: MutableList<ActiveSection> = mutableListOf()
) : RootSection {

    override var result: SimpleTreeSectionResult? = null
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

    override fun end(): TreeSectionResult {
        this.endTime = System.nanoTime()

        val root = result.also {
            it?.startTime = endTime
            it?.endTime = endTime
        } ?: SimpleTreeSectionResult(
            name = name,
            startTime = startTime,
            endTime = endTime,
        )

        this.child.forEach { child ->
            (child.result as? SimpleSectionResult)?.recurse(root)
        }

        this.result = root

        reset()

        return root
    }

    override fun reset() {
        startTime = System.nanoTime()
        endTime = 0
    }

    companion object {
        private fun SimpleSectionResult.recurse(root: SimpleTreeSectionResult) {
            val startSection = root.getResult(this.name).also {
                it?.name = this.name
                it?.startTime = this.startTime
                it?.endTime = this.endTime
                it?.root = root
                it?.usage = SimpleTreeSectionResult.calculateUsage(it, it?.duration ?: 0)
            } ?:
                SimpleTreeSectionResult(
                    name = this.name,
                    startTime = this.startTime,
                    endTime = this.endTime,
                    root = root,
                )

            this.child.forEach { child ->
                (child as? SimpleSectionResult)?.recurse(root)
            }

            root.addResult(startSection)
        }
    }
}
