package arc.profiler.section

internal data class ArcRootSection(
    override var name: String = "root",
    override var startTime: Long = System.nanoTime(),
    override val child: MutableList<ActiveSection> = mutableListOf()
) : RootSection {

    override var result: ArcTreeSectionResult? = null
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

    override fun end(): TreeSectionResult {
        this.endTime = System.nanoTime()

        val root = result.also {
            it?.startTime = endTime
            it?.endTime = endTime
        } ?: ArcTreeSectionResult(
            name = name,
            startTime = startTime,
            endTime = endTime,
        )

        this.child.forEach { child ->
            (child.result as? ArcSectionResult)?.recurse(root)
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
        private fun ArcSectionResult.recurse(root: ArcTreeSectionResult) {
            val startSection = root.getResult(this.name).also {
                it?.name = this.name
                it?.startTime = this.startTime
                it?.endTime = this.endTime
                it?.root = root
                it?.usage = ArcTreeSectionResult.calculateUsage(it, it?.duration ?: 0)
            } ?:
                ArcTreeSectionResult(
                    name = this.name,
                    startTime = this.startTime,
                    endTime = this.endTime,
                    root = root,
                )

            this.child.forEach { child ->
                (child as? ArcSectionResult)?.recurse(root)
            }

            root.addResult(startSection)
        }
    }

}