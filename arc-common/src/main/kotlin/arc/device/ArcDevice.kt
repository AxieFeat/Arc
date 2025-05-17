package arc.device

import arc.OSPlatform
import oshi.SystemInfo

internal object ArcDevice : Device {

    private val systemInfo = SystemInfo()

    override val os: OSPlatform = OSPlatform.fromString(systemInfo.operatingSystem.family.lowercase()).also {
        if(it == OSPlatform.UNKNOWN) {
            println("OS not supported: ${systemInfo.operatingSystem.family.lowercase()}")
        }
    }

    override val java: String = Runtime.version().version().joinToString(".")
    override val cpu: CPU = ArcCPU(systemInfo)
    override val gpu: List<GPU> = systemInfo.hardware.graphicsCards.map { ArcGPU(it) }
    override val powerSources: List<PowerSource> = systemInfo.hardware.powerSources.map { ArcPowerSource(it) }
    override val motherBoard: MotherBoard = ArcMotherBoard(systemInfo)
    override val ram: RAM = ArcRAM(systemInfo)
    override val model: String = systemInfo.hardware.computerSystem.model
    override val serial: String = systemInfo.hardware.computerSystem.serialNumber
    override val uuid: String = systemInfo.hardware.computerSystem.hardwareUUID
    override val manufacturer: String = systemInfo.hardware.computerSystem.manufacturer

}