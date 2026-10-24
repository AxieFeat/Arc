package arc.gles.graphics

import arc.graphics.ContextResource
import org.lwjgl.opengles.GLES
import org.lwjgl.opengles.GLESCapabilities
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicLong

/** Native reset detection/notification is a host responsibility; replacing capabilities identifies a new context. */
internal object GlesContextIdentity {

    private val identities = WeakHashMap<GLESCapabilities, Long>()
    private val next = AtomicLong(Long.MIN_VALUE)
    private val cached = ThreadLocal<Pair<GLESCapabilities, Long>>()
    fun current(): Long {
        val capabilities = try {
            GLES.getCapabilities()
        } catch (_: IllegalStateException) {
            cached.remove()
            return 0
        }
        cached.get()?.let { if (it.first === capabilities) return it.second }
        val generation = synchronized(identities) { identities.getOrPut(capabilities) { next.incrementAndGet() } }
        cached.set(capabilities to generation)
        return generation
    }
}

internal abstract class GlesContextResource : ContextResource {

    final override val resourceGeneration = GlesContextIdentity.current().also { check(it != 0L) { "An GLES context must be current" } }
    private val owner = Thread.currentThread()
    protected fun isCurrentContext(): Boolean {
        check(Thread.currentThread() === owner) { "Graphics resource operations require the creating thread" }
        return GlesContextIdentity.current() == resourceGeneration
    }

    protected fun requireCurrentContext() {
        check(Thread.currentThread() === owner) { "Graphics resource operations require the creating thread" }
        check(isCurrentContext()) { "Graphics resource belongs to an old or missing context; recreate it for the current generation" }
    }
}
