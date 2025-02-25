package arc.device

import oshi.SystemInfo

internal data class ArcCPU(
    private val systemInfo: SystemInfo
): CPU {

    override val name: String = systemInfo.hardware.processor.processorIdentifier.name
    override val id: String = systemInfo.hardware.processor.processorIdentifier.identifier
    override val family: String = systemInfo.hardware.processor.processorIdentifier.family
    override val vendor: String = systemInfo.hardware.processor.processorIdentifier.vendor
    override val architecture: String = systemInfo.hardware.processor.processorIdentifier.microarchitecture
    override val model: String = systemInfo.hardware.processor.processorIdentifier.model

}