package arc.vk

import arc.AbstractApplicationBackend
import arc.vk.device.VkOshiDevice

internal object VkApplicationBackend : AbstractApplicationBackend("vulkan", VkOshiDevice) {

    override val version: String = "unknown" // TODO
}
