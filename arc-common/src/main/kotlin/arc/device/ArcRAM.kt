package arc.device

import oshi.SystemInfo

internal data class ArcRAM(
    private val systemInfo: SystemInfo
) : RAM {
    override val available: Long
        get() = systemInfo.hardware.memory.available
    override val total: Long
        get() = systemInfo.hardware.memory.total
    override val swapUsed: Long
        get() = systemInfo.hardware.memory.virtualMemory.swapUsed
    override val swapTotal: Long
        get() = systemInfo.hardware.memory.virtualMemory.swapTotal
}