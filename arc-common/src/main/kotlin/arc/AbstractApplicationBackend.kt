package arc

import arc.device.ArcDevice
import arc.device.Device

abstract class AbstractApplicationBackend(
    override val name: String
) : ApplicationBackend {

    override val device: Device = ArcDevice

}