package arc.device

import oshi.hardware.GraphicsCard

internal data class OshiGPU(
    private val graphicsCard: GraphicsCard
) : GPU {

    override val name: String = graphicsCard.name
    override val isIntegrated: Boolean = iGpuKeywords.any { keyword ->
        val trimmedName = name.trim()
        trimmedName.contains(keyword, ignoreCase = true) &&
                iGpuExcludeKeywords.none { trimmedName.contains(it, ignoreCase = true) }
    }
    override val id: String = graphicsCard.deviceId
    override val vRam: Long = graphicsCard.vRam
    override val vendor: String = graphicsCard.vendor
    override val info: String = graphicsCard.versionInfo

    companion object {
        private val iGpuKeywords = listOf(
            // Intel iGPU's
            "UHD",
            "Iris",
            "HD Graphics",

            // AMD iGPU's
            "Vega",
            "Radeon Graphics",

            // Apple iGPU
            "Apple"
        )

        private val iGpuExcludeKeywords = listOf(
            // Nvidia dGPU's
            "GeForce",
            "RTX",
            "GTX",
            "GT",
            "Quadro",
            "Tesla",
            "TITAN",
            "GRID",

            // AMD dGPU's
            "RX",
            "Radeon Pro",
            "Radeon Fury",
            "FirePro",

            // Intel dGPU
            "Arc"
        )
    }
}
