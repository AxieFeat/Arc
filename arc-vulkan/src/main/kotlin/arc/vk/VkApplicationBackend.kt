package arc.vk

import arc.AbstractApplicationBackend

internal object VkApplicationBackend : AbstractApplicationBackend("vulkan") {

    override val version: String = "unknown" // TODO

    override val isIGpu: Boolean = true // TODO

}