package arc

import arc.device.OshiDevice
import arc.device.Device

abstract class AbstractApplicationBackend(
    override val name: String
) : ApplicationBackend {

    override val device: Device = OshiDevice

}