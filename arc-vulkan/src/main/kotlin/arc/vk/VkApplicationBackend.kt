package arc.vk

import arc.AbstractApplicationBackend

internal object VkApplicationBackend : AbstractApplicationBackend("vulkan") {

    override val version: String = "unknown"

    override val isIGpu: Boolean = true

}