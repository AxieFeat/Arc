package arc.profiler.section

import kotlin.math.roundToInt

internal data class ArcTreeSectionResult(
    override var name: String,
    override var root: TreeSectionResult? = null,
    override var startTime: Long,
    override var endTime: Long = System.nanoTime(),
    override var child: MutableList<ArcTreeSectionResult> = mutableListOf(),
) : TreeSectionResult {

    override var usage: Double = calculateUsage(root, duration)

    override val duration: Long
        get() = endTime - startTime

    fun addResult(child: ArcTreeSectionResult) {
        this.child.add(child)
    }

    fun getResult(child: String): ArcTreeSectionResult? {
        return this.child.find { it.name == child }
    }

    override fun pretty(level: Int): String {
        val sectionInfo = if(level > 0) {
            val indent = "  ".repeat(level)
            "$indent- $name: ${(duration / 1000000.0).round()}ms, ${usage}%"
        } else {
            "${name.uppercase()}: ${(duration / 1000000.0).round()}ms, ${usage}%"
        }

        if (child.isEmpty()) {
            return sectionInfo
        }

        val childInfo = child.joinToString("\n") { it.pretty(level + 1) }

        return "$sectionInfo\n$childInfo"
    }

    override fun toString(): String {
        return pretty()
    }

    companion object {
        fun calculateUsage(root: TreeSectionResult?, duration: Long): Double {
            val parent = root ?: return 100.0
            val parentDuration = parent.duration

            if (parentDuration == 0L || duration == 0L) return 0.0

            val percentage = (duration.toDouble() / parentDuration) * 100
            return percentage.round()
        }

        private fun Double.round(): Double {
            return (this * 100).roundToInt() / 100.0
        }
    }
}