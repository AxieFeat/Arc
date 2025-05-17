package arc.device

import java.time.LocalDate

/**
 * This interface represents power source of [Device].
 *
 * Note: On some OC values may not be present.
 *
 * @see Device
 */
interface PowerSource {

    /**
     * Name of PowerSource at the operating system level.
     *
     * @return The power source name, as reported by the operating system.
     */
    val name: String

    /**
     * Name of PowerSource at the device level.
     *
     * @return The power source name, as reported by the device itself.
     */
    val deviceName: String

    /**
     * Estimated remaining capacity as a fraction of max capacity.
     *
     * This is an estimated/smoothed value which should correspond to the Operating System's "percent power" display,
     * and may not directly correspond to the ratio of [currentCapacity] to [maxCapacity].
     *
     * @return A value between 0.0 (fully drained) and 1.0 (fully charged).
     */
    val remainingCapacityPercent: Double

    /**
     * Estimated time remaining on the power source, in seconds, as reported by the operating system.
     *
     * This is an estimated/smoothed value which should correspond to the Operating System's "battery time remaining"
     * display, and will react slowly to changes in power consumption.
     *
     * @return If positive, seconds remaining. If negative, -1.0 (calculating) or -2.0 (unlimited).
     */
    val timeRemainingEstimated: Double

    /**
     * Estimated time remaining on the power source, in seconds, as reported by the battery. If the battery is charging,
     * this value may represent time remaining to fully charge the battery.
     *
     * Note that this value is not very accurate on some battery systems. The value may vary widely depending on present
     * power usage, which could be affected by disk activity and other factors. This value will often be a higher value
     * than [timeRemainingEstimated].
     *
     * @return Seconds remaining to fully discharge or fully charge the battery.
     */
    val timeRemainingInstant: Double

    /**
     * Power Usage Rate of the battery, in milliWatts (mW).
     *
     * @return If positive, the charge rate. If negative, the discharge rate.
     */
    val powerUsageRate: Double

    /**
     * Voltage of the battery, in Volts.
     *
     * @return The battery voltage, or -1 if unknown.
     */
    val voltage: Double

    /**
     * Amperage of the battery, in milliAmperes (mA).
     *
     * @return The battery amperage. If positive, charging the battery. If negative, discharging the battery.
     */
    val amperage: Double

    /**
     * Reports whether the device is plugged in to an external power source.
     *
     * @return `true` if plugged in, `false` otherwise.
     */
    val powerOnLine: Boolean

    /**
     * Reports whether the battery is charging.
     *
     * @return `true` if the battery is charging, `false` otherwise.
     */
    val charging: Boolean

    /**
     * Reports whether the battery is discharging.
     *
     * @return `true` if the battery is discharging, `false` otherwise.
     */
    val discharging: Boolean

    /**
     * Reports the units of [currentCapacity], [maxCapacity], and [designCapacity]
     *
     * @return The units of battery capacity.
     */
    val capacityUnits: CapacityUnits

    /**
     * The current (remaining) capacity of the battery.
     *
     * @return The current capacity. Units are defined by [.getCapacityUnits].
     */
    val currentCapacity: Int

    /**
     * The maximum capacity of the battery. When compared to design capacity, permits a measure of battery state of
     * health. It is possible for max capacity to exceed design capacity.
     *
     * @return The maximum capacity. Units are defined by [capacityUnits].
     */
    val maxCapacity: Int

    /**
     * The design (original) capacity of the battery. When compared to maximum capacity, permits a measure of battery
     * state of health. It is possible for max capacity to exceed design capacity.
     *
     * @return The design capacity. Units are defined by [capacityUnits].
     */
    val designCapacity: Int

    /**
     * The cycle count of the battery, if known.
     *
     * @return The cycle count of the battery, or -1 if unknown.
     */
    val cycleCount: Int

    /**
     * The battery chemistry (e.g., Lithium Ion).
     *
     * @return The battery chemistry.
     */
    val chemistry: String

    /**
     * The battery's date of manufacture.
     *
     * Some battery manufacturers encode the manufacture date in the serial number. Parsing this value is operating
     * system and battery manufacturer dependent, and is left to the user.
     *
     * @return The manufacture date, if available. May be `null`.
     */
    val manufactureDate: LocalDate?

    /**
     * The name of the battery's manufacturer.
     *
     * @return The manufacturer name.
     */
    val manufacturer: String

    /**
     * The battery's serial number.
     *
     * Some battery manufacturers encode the manufacture date in the serial number. Parsing this value is operating
     * system and battery manufacturer dependent, and is left to the user.
     *
     * @return The serial number.
     */
    val serialNumber: String

    /**
     * The battery's temperature, in degrees Celsius.
     *
     * @return The battery's temperature, or 0 if uknown.
     */
    val temperature: Double

    /**
     * Units of Battery Capacity
     */
    enum class CapacityUnits {
        /**
         * MilliWattHours (mWh).
         */
        MWH,

        /**
         * MilliAmpHours (mAh). Should be multiplied by voltage to convert to mWh.
         */
        MAH,

        /**
         * Relative units. The specific units are not defined. The ratio of current/max capacity still represents state
         * of charge and the ratio of max/design capacity still represents state of health.
         */
        RELATIVE
    }

}