package arc.device

import arc.OSPlatform
import oshi.PlatformEnum
import oshi.SystemInfo

internal object OshiDevice : Device {

    private val systemInfo = SystemInfo()

    override val os: OSPlatform = SystemInfo.getCurrentPlatform().toOsPlatform().also {
        if(it == OSPlatform.UNKNOWN) {
            println("OS not supported: ${SystemInfo.getCurrentPlatform().name}")
        }
    }

    override val java: String = Runtime.version().version().joinToString(".")
    override val cpu: CPU = OshiCPU(systemInfo)
    override val gpu: List<GPU> = systemInfo.hardware.graphicsCards.map { OshiGPU(it) }
    override val powerSources: List<PowerSource> = systemInfo.hardware.powerSources.map { OshiPowerSource(it) }
    override val motherBoard: MotherBoard = OshiMotherBoard(systemInfo)
    override val ram: RAM = OshiRAM(systemInfo)
    override val model: String = systemInfo.hardware.computerSystem.model
    override val serial: String = systemInfo.hardware.computerSystem.serialNumber
    override val uuid: String = systemInfo.hardware.computerSystem.hardwareUUID
    override val manufacturer: String = systemInfo.hardware.computerSystem.manufacturer

    private fun PlatformEnum.toOsPlatform(): OSPlatform {
        return when(this) {
            PlatformEnum.WINDOWS -> OSPlatform.WINDOWS
            PlatformEnum.LINUX -> OSPlatform.LINUX
            PlatformEnum.MACOS -> OSPlatform.MACOS
            PlatformEnum.ANDROID -> OSPlatform.ANDROID

            else -> OSPlatform.UNKNOWN
        }
    }

}