package arc.profiler.section

import java.math.BigDecimal
import java.math.RoundingMode

internal data class ArcTreeSectionResult(
    override val name: String,
    override var root: TreeSectionResult? = null,
    override val startTime: Long,
    override val endTime: Long = System.nanoTime(),
    override val child: MutableList<TreeSectionResult> = mutableListOf(),
) : TreeSectionResult {

    override val usage: Double = run {
        val parent = root ?: return@run 100.0
        val parentDuration = parent.duration

        if (parentDuration == 0L || duration == 0L) return@run 0.0

        val percentage = (duration.toDouble() / parentDuration) * 100
        return@run BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    override val duration: Long
        get() = endTime - startTime

    fun addResult(child: TreeSectionResult) {
        this.child.add(child)
    }

    override fun pretty(level: Int): String {
        val sectionInfo = if(level > 0) {
            val indent = "  ".repeat(level)

            "$indent- $name: ${duration / 1000000}ms, ${usage}%"
        } else {
            "${name.uppercase()}: ${duration / 1000000}ms, ${usage}%"
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
}