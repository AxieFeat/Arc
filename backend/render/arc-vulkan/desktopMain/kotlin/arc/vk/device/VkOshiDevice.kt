package arc.vk.device

import arc.device.GPU
import arc.device.OshiDevice
import arc.vk.VkApplication

internal object VkOshiDevice : OshiDevice() {

    override fun findUsedGpu(): GPU? {
        val renderer = VkApplication.physicalDevice.deviceName.trim()

        return gpu.find {
            it.name.trim().equals(renderer, true)
        }
    }
}
