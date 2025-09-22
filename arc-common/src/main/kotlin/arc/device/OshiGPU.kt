package arc.device

import oshi.hardware.GraphicsCard

internal data class OshiGPU(
    private val graphicsCard: GraphicsCard
) : GPU {

    override val name: String = graphicsCard.name
    override val integrated: Boolean = iGpuKeywords.any { keyword ->
        name.trim().contains(keyword, ignoreCase = true)
    }
    override val id: String = graphicsCard.deviceId
    override val vRam: Long = graphicsCard.vRam
    override val vendor: String = graphicsCard.vendor
    override val info: String = graphicsCard.versionInfo

    companion object {
        private val iGpuKeywords = listOf("UHD", "Iris", "HD Graphics", "Apple", "Intel")
    }
}
