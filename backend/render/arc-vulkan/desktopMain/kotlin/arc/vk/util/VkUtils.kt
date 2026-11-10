package arc.vk.util

import org.lwjgl.vulkan.VK10

internal object VkUtils {

    @JvmStatic
    fun vkCheck(err: Int, errMsg: String) {
        if (err != VK10.VK_SUCCESS) {
            throw RuntimeException("$errMsg: $err")
        }
    }
}
