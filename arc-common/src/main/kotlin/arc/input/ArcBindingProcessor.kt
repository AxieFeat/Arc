package arc.input

internal class ArcBindingProcessor : BindingProcessor {

    override val bindings: MutableList<BindingLike> = mutableListOf()

    override fun bind(binding: BindingLike) {
        if(bindings.any { it.id == binding.id }) {
            return
        }
        bindings.add(binding)
    }

    override fun unbind(binding: BindingLike) {
        bindings.remove(binding)
    }

    override fun unbind(id: String) {
        bindings.remove(get(id))
    }

    override fun get(id: String): BindingLike? {
        return bindings.firstOrNull { it.id == id }
    }

}