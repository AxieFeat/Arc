package arc.device

import oshi.SystemInfo

internal data class OshiMotherBoard(
    private val systemInfo: SystemInfo
) : MotherBoard {

    override val serial: String = systemInfo.hardware.computerSystem.baseboard.serialNumber
    override val model: String = systemInfo.hardware.computerSystem.baseboard.model
    override val version: String = systemInfo.hardware.computerSystem.baseboard.version
    override val manufacturer: String = systemInfo.hardware.computerSystem.baseboard.manufacturer

}