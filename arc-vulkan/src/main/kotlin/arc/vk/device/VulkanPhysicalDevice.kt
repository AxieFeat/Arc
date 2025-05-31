package arc.vk.device

import arc.util.pattern.Cleanable
import arc.vk.VulkanInstance
import arc.vk.util.VkUtils.vkCheck
import org.lwjgl.PointerBuffer
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.*

// The class name is VulkanPhysicalDevice because in LWJGL exists VkPhysicalDevice
class VulkanPhysicalDevice private constructor(
    val vkPhysicalDevice: VkPhysicalDevice
) : Cleanable {

    private val vkDeviceExtensions: VkExtensionProperties.Buffer
    val vkMemoryProperties: VkPhysicalDeviceMemoryProperties
    val vkPhysicalDeviceFeatures: VkPhysicalDeviceFeatures
    val vkPhysicalDeviceProperties: VkPhysicalDeviceProperties
    val vkQueueFamilyProps: VkQueueFamilyProperties.Buffer

    val deviceName: String
        get() = vkPhysicalDeviceProperties.deviceNameString()

    init {
        MemoryStack.stackPush().use { stack ->
            val intBuffer = stack.mallocInt(1)

            // Get device properties
            vkPhysicalDeviceProperties = VkPhysicalDeviceProperties.calloc()
            VK10.vkGetPhysicalDeviceProperties(vkPhysicalDevice, vkPhysicalDeviceProperties)

            // Get device extensions
            vkCheck(
                VK10.vkEnumerateDeviceExtensionProperties(vkPhysicalDevice, null as String?, intBuffer, null),
                "Failed to get number of device extension properties"
            )
            vkDeviceExtensions = VkExtensionProperties.calloc(intBuffer.get(0))
            vkCheck(
                VK10.vkEnumerateDeviceExtensionProperties(
                    vkPhysicalDevice,
                    null as String?,
                    intBuffer,
                    vkDeviceExtensions
                ),
                "Failed to get extension properties"
            )

            // Get Queue family properties
            VK10.vkGetPhysicalDeviceQueueFamilyProperties(vkPhysicalDevice, intBuffer, null)
            vkQueueFamilyProps = VkQueueFamilyProperties.calloc(intBuffer.get(0))
            VK10.vkGetPhysicalDeviceQueueFamilyProperties(vkPhysicalDevice, intBuffer, vkQueueFamilyProps)

            vkPhysicalDeviceFeatures = VkPhysicalDeviceFeatures.calloc()
            VK10.vkGetPhysicalDeviceFeatures(vkPhysicalDevice, vkPhysicalDeviceFeatures)

            // Get Memory information and properties
            vkMemoryProperties = VkPhysicalDeviceMemoryProperties.calloc()
            VK10.vkGetPhysicalDeviceMemoryProperties(vkPhysicalDevice, vkMemoryProperties)
        }
    }

    override fun cleanup() {
        vkMemoryProperties.free()
        vkPhysicalDeviceFeatures.free()
        vkQueueFamilyProps.free()
        vkDeviceExtensions.free()
        vkPhysicalDeviceProperties.free()
    }

    private fun hasGraphicsQueueFamily(): Boolean {
        return vkQueueFamilyProps.any { familyProps ->
            (familyProps.queueFlags() and VK10.VK_QUEUE_GRAPHICS_BIT) != 0
        }
    }

    private fun hasKHRSwapChainExtension(): Boolean {
        return vkDeviceExtensions.any { it.extensionNameString() == KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME }
    }

    companion object {

        @JvmStatic
        fun createPhysicalDevice(instance: VulkanInstance, prefferredDeviceName: String? = null): VulkanPhysicalDevice {
            var selectedPhysicalDevice: VulkanPhysicalDevice? = null
            MemoryStack.stackPush().use { stack ->
                // Get available devices
                val physicalDevices = getPhysicalDevices(instance, stack)
                val numDevices = physicalDevices.capacity()
                if (numDevices <= 0) {
                    throw RuntimeException("No physical devices found")
                }

                // Populate available devices
                val devices: MutableList<VulkanPhysicalDevice> = mutableListOf()
                for (i in 0..<numDevices) {
                    val vkPhysicalDevice = VkPhysicalDevice(physicalDevices.get(i), instance.vkInstance)
                    val physicalDevice = VulkanPhysicalDevice(vkPhysicalDevice)

                    val deviceName = physicalDevice.deviceName

                    if (physicalDevice.hasGraphicsQueueFamily() && physicalDevice.hasKHRSwapChainExtension()) {
                        if (prefferredDeviceName != null && prefferredDeviceName == deviceName) {
                            selectedPhysicalDevice = physicalDevice
                            break
                        }
                        devices.add(physicalDevice)
                    } else {
                        physicalDevice.cleanup()
                    }
                }

                // No preferred device or it does not meet requirements, just pick the first one
                selectedPhysicalDevice =
                    if (selectedPhysicalDevice == null && !devices.isEmpty()) devices.removeAt(0) else selectedPhysicalDevice

                // Clean up non-selected devices
                for (physicalDevice in devices) {
                    physicalDevice.cleanup()
                }

                if (selectedPhysicalDevice == null) {
                    throw RuntimeException("No suitable physical devices found")
                }
            }
            return selectedPhysicalDevice!!
        }

        @JvmStatic
        fun getPhysicalDevices(instance: VulkanInstance, stack: MemoryStack): PointerBuffer {
            val intBuffer = stack.mallocInt(1)
            vkCheck(
                VK10.vkEnumeratePhysicalDevices(instance.vkInstance, intBuffer, null),
                "Failed to get number of physical devices"
            )
            val numDevices = intBuffer.get(0)

            val physicalDevices = stack.mallocPointer(numDevices)
            vkCheck(
                VK10.vkEnumeratePhysicalDevices(instance.vkInstance, intBuffer, physicalDevices),
                "Failed to get physical devices"
            )
            return physicalDevices
        }
    }
}