package arc.input

/**
 * This interface represents object, that can use bindings.
 */
interface BindingHolder {

    /**
     * Processor for bindings.
     */
    val bindingProcessor: BindingProcessor
}
