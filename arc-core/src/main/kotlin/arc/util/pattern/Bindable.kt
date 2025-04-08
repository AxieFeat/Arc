package arc.util.pattern

/**
 * This interface represents a resource that can be bound to and unbound from a rendering context.
 */
interface Bindable {

    /**
     * Binds the resource, making it active in current context.
     */
    fun bind()

    /**
     * Unbinds the resource, deactivating it from current context.
     */
    fun unbind()

}