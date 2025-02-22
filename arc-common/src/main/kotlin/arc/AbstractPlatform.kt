package arc

import arc.device.ArcDevice
import arc.device.Device

abstract class AbstractPlatform(
    override val id: String
) : Platform {

    override val device: Device = ArcDevice

}