package arc.profiler

data class ArcProfiler(
    override val sections: MutableSet<String> = mutableSetOf(),
    override val results: MutableSet<SectionResult> = mutableSetOf()
) : Profiler {

    private val time = mutableMapOf<String, Long>()

    override fun startSection(name: String) {
        time[name] = System.nanoTime()
        sections.add(name)
    }

    override fun endSection(name: String): SectionResult {
        val result = ArcSectionResult(
            name,
            time[name] ?: 0L,
        )
        time.remove(name)
        results.add(result)

        return result
    }

    override fun clear() {
        sections.clear()
        results.clear()
    }

    object Factory : Profiler.Factory {
        override fun create(): Profiler {
            return ArcProfiler()
        }
    }

}