package arc.device

import java.time.LocalDate

internal data class ArcPowerSource(
    private val powerSource: oshi.hardware.PowerSource
) : PowerSource {

    override val name: String = powerSource.name
    override val deviceName: String = powerSource.deviceName

    override val remainingCapacityPercent: Double
        get() = powerSource.remainingCapacityPercent

    override val timeRemainingEstimated: Double
        get() = powerSource.timeRemainingEstimated

    override val timeRemainingInstant: Double
        get() = powerSource.timeRemainingInstant

    override val powerUsageRate: Double
        get() = powerSource.powerUsageRate

    override val voltage: Double
        get() = powerSource.voltage

    override val amperage: Double
        get() = powerSource.amperage

    override val powerOnLine: Boolean
        get() = powerSource.isPowerOnLine
    override val charging: Boolean
        get() = powerSource.isCharging
    override val discharging: Boolean
        get() = powerSource.isDischarging
    override val capacityUnits: PowerSource.CapacityUnits
        get() = when(powerSource.capacityUnits) {
            oshi.hardware.PowerSource.CapacityUnits.MWH -> PowerSource.CapacityUnits.MWH
            oshi.hardware.PowerSource.CapacityUnits.MAH -> PowerSource.CapacityUnits.MAH
            oshi.hardware.PowerSource.CapacityUnits.RELATIVE -> PowerSource.CapacityUnits.RELATIVE
        }
    override val currentCapacity: Int
        get() = powerSource.currentCapacity
    override val maxCapacity: Int
        get() = powerSource.maxCapacity
    override val designCapacity: Int
        get() = powerSource.designCapacity
    override val cycleCount: Int
        get() = powerSource.cycleCount
    override val chemistry: String
        get() = powerSource.chemistry
    override val manufactureDate: LocalDate?
        get() = powerSource.manufactureDate
    override val manufacturer: String
        get() = powerSource.manufacturer
    override val serialNumber: String
        get() = powerSource.serialNumber
    override val temperature: Double
        get() = powerSource.temperature
}