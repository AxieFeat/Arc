@file:JvmSynthetic
package arc.profiler

import arc.profiler.section.ActiveSection
import arc.profiler.section.SectionResult
import arc.profiler.section.TreeSectionResult

private var context: Profiler = Profiler.create()
private var parent: ActiveSection = context.root
private val sectionStack = ArrayDeque<ActiveSection>()

/**
 * Set context of this extra functions.
 *
 * @param profiler Profiler for context.
 */
@JvmSynthetic
fun setProfilerContext(profiler: Profiler) {
    context = profiler
    parent = profiler.root
    sectionStack.clear()
}

/**
 * Create section, execute code and end section.
 *
 * @param name Name of section.
 * @param callback Code for call.
 *
 * @return Instance of [SectionResult].
 */
@JvmSynthetic
fun section(name: String, callback: () -> Unit): SectionResult {
    begin(name)
    callback.invoke()
    return end(name)
}

/**
 * Begin section in current context.
 *
 * @param name Name of section.
 *
 * @return Instance of [ActiveSection].
 */
@JvmSynthetic
fun begin(name: String): ActiveSection {
    sectionStack.addLast(parent)
    return parent.start(name).also { parent = it }
}

/**
 * End section in current context.
 *
 * @param name Name of section.
 *
 * @return Instance of [SectionResult].
 */
@JvmSynthetic
fun end(name: String): SectionResult {
    require(parent.name == name) { "Trying to end section '$name', but current is '${parent.name}'" }

    val result = parent.end()

    parent = if (sectionStack.isNotEmpty()) sectionStack.removeLast() else context.root

    return result
}

/**
 * End root section.
 *
 * @return Instance of [TreeSectionResult].
 */
@JvmSynthetic
fun end(): TreeSectionResult = context.end()