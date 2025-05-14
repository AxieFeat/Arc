package arc.input

internal abstract class AbstractInputDevice(
    override val name: String,
    override val type: DeviceType,
    override val bindingProcessor: BindingProcessor = ArcBindingProcessor()
) : InputDevice