package arc.input

class ArcBindingProcessor : BindingProcessor {

    override val bindings: MutableList<Binding> = mutableListOf()

    override fun bind(binding: Binding) {
        bindings.add(binding)
    }

    override fun unbind(binding: Binding) {
        bindings.remove(binding)
    }

    override fun unbind(id: String) {
        bindings.remove(get(id))
    }

    override fun get(id: String): Binding? {
        return bindings.firstOrNull { it.id == id }
    }

}