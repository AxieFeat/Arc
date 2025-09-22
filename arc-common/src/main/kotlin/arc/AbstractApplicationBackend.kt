package arc

import arc.device.OshiDevice
import arc.device.Device

abstract class AbstractApplicationBackend(
    override val name: String,
    override val device: OshiDevice
) : ApplicationBackend
