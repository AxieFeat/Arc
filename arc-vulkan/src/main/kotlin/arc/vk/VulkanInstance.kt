package arc.vk

import arc.OSPlatform
import arc.vk.util.VkUtils.vkCheck
import org.lwjgl.PointerBuffer
import org.lwjgl.glfw.GLFWVulkan
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.vulkan.*

// The class name is VulkanInstance because in LWJGL exists VkInstance
internal class VulkanInstance(
    useValidationLayer: Boolean = false
) {

    val vkInstance: VkInstance

    private var debugUtils: VkDebugUtilsMessengerCreateInfoEXT? = null
    private var vkDebugHandle: Long = 0

    init {
        MemoryStack.stackPush().use { stack ->
            // Create application information
            val appShortName = stack.UTF8("VulkanBook")
            val appInfo = VkApplicationInfo.calloc(stack)
                .sType(VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO)
                .pApplicationName(appShortName)
                .applicationVersion(1)
                .pEngineName(appShortName)
                .engineVersion(0)
                .apiVersion(VK11.VK_API_VERSION_1_1)

            // Validation layers
            val validationLayers = getSupportedValidationLayers()
            val numValidationLayers = validationLayers.size
            var supportsValidation = useValidationLayer
            if (useValidationLayer && numValidationLayers == 0) {
                supportsValidation = false
                println("Request validation but no supported validation layers found. Falling back to no validation")
            }

            // Set required  layers
            var requiredLayers: PointerBuffer? = null
            if (supportsValidation) {
                requiredLayers = stack.mallocPointer(numValidationLayers)
                for (i in 0..<numValidationLayers) {
                    validationLayers[i]?.let { requiredLayers.put(i, stack.ASCII(it)) }
                }
            }

            val instanceExtensions = getInstanceExtensions()

            // GLFW Extension
            val glfwExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions()
            if (glfwExtensions == null) {
                throw RuntimeException("Failed to find the GLFW platform surface extensions")
            }

            val requiredExtensions: PointerBuffer?

            val usePortability = instanceExtensions.contains(PORTABILITY_EXTENSION) &&
                    VkApplicationBackend.device.os == OSPlatform.MACOS
            if (supportsValidation) {
                val vkDebugUtilsExtension = stack.UTF8(EXTDebugUtils.VK_EXT_DEBUG_UTILS_EXTENSION_NAME)
                val numExtensions =
                    if (usePortability) glfwExtensions.remaining() + 2 else glfwExtensions.remaining() + 1
                requiredExtensions = stack.mallocPointer(numExtensions)
                requiredExtensions.put(glfwExtensions).put(vkDebugUtilsExtension)
                if (usePortability) {
                    requiredExtensions.put(stack.UTF8(PORTABILITY_EXTENSION))
                }
            } else {
                val numExtensions = if (usePortability) glfwExtensions.remaining() + 1 else glfwExtensions.remaining()
                requiredExtensions = stack.mallocPointer(numExtensions)
                requiredExtensions.put(glfwExtensions)
                if (usePortability) {
                    requiredExtensions.put(stack.UTF8(KHRPortabilitySubset.VK_KHR_PORTABILITY_SUBSET_EXTENSION_NAME))
                }
            }
            requiredExtensions.flip()

            var extension = MemoryUtil.NULL
            if (supportsValidation) {
                debugUtils = createDebugCallBack()
                extension = debugUtils!!.address()
            }

            // Create instance info
            val instanceInfo = VkInstanceCreateInfo.calloc(stack)
                .sType(VK10.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
                .pNext(extension)
                .pApplicationInfo(appInfo)
                .ppEnabledLayerNames(requiredLayers)
                .ppEnabledExtensionNames(requiredExtensions)
            if (usePortability) {
                instanceInfo.flags(0x00000001) // VK_INSTANCE_CREATE_ENUMERATE_PORTABILITY_BIT_KHR
            }

            val pInstance = stack.mallocPointer(1)
            vkCheck(VK10.vkCreateInstance(instanceInfo, null, pInstance), "Error creating instance")
            vkInstance = VkInstance(pInstance.get(0), instanceInfo)

            vkDebugHandle = VK10.VK_NULL_HANDLE
            if (supportsValidation) {
                val longBuff = stack.mallocLong(1)
                vkCheck(
                    EXTDebugUtils.vkCreateDebugUtilsMessengerEXT(
                        vkInstance,
                        debugUtils!!,
                        null,
                        longBuff
                    ), "Error creating debug utils"
                )
                vkDebugHandle = longBuff.get(0)
            }
        }
    }

    fun cleanup() {
        if (vkDebugHandle != VK10.VK_NULL_HANDLE) {
            EXTDebugUtils.vkDestroyDebugUtilsMessengerEXT(vkInstance, vkDebugHandle, null)
        }
        VK10.vkDestroyInstance(vkInstance, null)
        if (debugUtils != null) {
            debugUtils!!.pfnUserCallback().free()
            debugUtils!!.free()
        }
    }

    private fun getInstanceExtensions(): MutableSet<String?> {
        val instanceExtensions: MutableSet<String?> = HashSet<String?>()
        MemoryStack.stackPush().use { stack ->
            val numExtensionsBuf = stack.callocInt(1)
            VK10.vkEnumerateInstanceExtensionProperties(null as String?, numExtensionsBuf, null)
            val numExtensions = numExtensionsBuf.get(0)

            val instanceExtensionsProps = VkExtensionProperties.calloc(numExtensions, stack)
            VK10.vkEnumerateInstanceExtensionProperties(null as String?, numExtensionsBuf, instanceExtensionsProps)
            for (i in 0..<numExtensions) {
                val props = instanceExtensionsProps.get(i)
                val extensionName = props.extensionNameString()
                instanceExtensions.add(extensionName)
            }
        }
        return instanceExtensions
    }

    private fun getSupportedValidationLayers(): MutableList<String?> {
        MemoryStack.stackPush().use { stack ->
            val numLayersArr = stack.callocInt(1)
            VK10.vkEnumerateInstanceLayerProperties(numLayersArr, null)
            val numLayers = numLayersArr.get(0)

            val propsBuf = VkLayerProperties.calloc(numLayers, stack)
            VK10.vkEnumerateInstanceLayerProperties(numLayersArr, propsBuf)
            val supportedLayers: MutableList<String?> = ArrayList<String?>()
            for (i in 0..<numLayers) {
                val props = propsBuf.get(i)
                val layerName = props.layerNameString()
                supportedLayers.add(layerName)
            }

            val layersToUse: MutableList<String?> = ArrayList<String?>()

            // Main validation layer
            if (supportedLayers.contains("VK_LAYER_KHRONOS_validation")) {
                layersToUse.add("VK_LAYER_KHRONOS_validation")
                return layersToUse
            }

            // Fallback 1
            if (supportedLayers.contains("VK_LAYER_LUNARG_standard_validation")) {
                layersToUse.add("VK_LAYER_LUNARG_standard_validation")
                return layersToUse
            }

            // Fallback 2 (set)
            val requestedLayers: MutableList<String?> = ArrayList<String?>()
            requestedLayers.add("VK_LAYER_GOOGLE_threading")
            requestedLayers.add("VK_LAYER_LUNARG_parameter_validation")
            requestedLayers.add("VK_LAYER_LUNARG_object_tracker")
            requestedLayers.add("VK_LAYER_LUNARG_core_validation")
            requestedLayers.add("VK_LAYER_GOOGLE_unique_objects")
            return requestedLayers.stream().filter { o: String? -> supportedLayers.contains(o) }.toList()
        }
    }

    companion object {
        const val MESSAGE_SEVERITY_BITMASK: Int =
                EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_SEVERITY_ERROR_BIT_EXT or
                EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_SEVERITY_WARNING_BIT_EXT

        const val MESSAGE_TYPE_BITMASK: Int =
                EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_TYPE_GENERAL_BIT_EXT or
                EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_TYPE_VALIDATION_BIT_EXT or
                EXTDebugUtils.VK_DEBUG_UTILS_MESSAGE_TYPE_PERFORMANCE_BIT_EXT

        private const val PORTABILITY_EXTENSION = "VK_KHR_portability_enumeration"

        private fun createDebugCallBack(): VkDebugUtilsMessengerCreateInfoEXT {
            return VkDebugUtilsMessengerCreateInfoEXT
                .calloc()
                .sType(EXTDebugUtils.VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT)
                .messageSeverity(MESSAGE_SEVERITY_BITMASK)
                .messageType(MESSAGE_TYPE_BITMASK)
                .pfnUserCallback { messageSeverity: Int, messageTypes: Int, pCallbackData: Long, pUserData: Long ->
                    val callbackData = VkDebugUtilsMessengerCallbackDataEXT.create(pCallbackData)
                    println("VkDebugUtilsCallback, ${callbackData.pMessageString()}")
                    VK10.VK_FALSE
                }
        }
    }
}