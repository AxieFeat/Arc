package arc.vk

/**
 * This object represents Vulkan implementation of engine.
 */
object Vulkan {

    /**
     * Preload Vulkan implementations.
     */
    @JvmStatic
    fun preload() {
        VkFactoryProvider.bootstrap()
    }
}
