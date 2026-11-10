package arc

import arc.device.OshiDevice

abstract class AbstractApplicationBackend(
    override val name: String,
    override val device: OshiDevice
) : ApplicationBackend
