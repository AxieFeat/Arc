package arc.gl.graphics

import arc.graphics.ContextResource
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GLCapabilities
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicLong

/** Native reset detection/notification is a host responsibility; replacing capabilities identifies a new context. */
internal object GlContextIdentity {

    private val identities = WeakHashMap<GLCapabilities, Long>()
    private val next = AtomicLong()
    private val cached = ThreadLocal<Pair<GLCapabilities, Long>>()
    fun current(): Long {
        val capabilities = try {
            GL.getCapabilities()
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

internal abstract class GlContextResource : ContextResource {

    final override val resourceGeneration = GlContextIdentity.current().also { check(it != 0L) { "An OpenGL context must be current" } }
    private val owner = Thread.currentThread()
    protected fun isCurrentContext(): Boolean {
        check(Thread.currentThread() === owner) { "Graphics resource operations require the creating thread" }
        return GlContextIdentity.current() == resourceGeneration
    }

    protected fun requireCurrentContext() {
        check(Thread.currentThread() === owner) { "Graphics resource operations require the creating thread" }
        check(isCurrentContext()) { "Graphics resource belongs to an old or missing context; recreate it for the current generation" }
    }
}
