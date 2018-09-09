/**
 * @author Nikolaus Knop
 */

package nikok.kprofile.api

import java.io.Serializable

interface Resource: Serializable

@Suppress("EnumEntryName")
enum class MemoryUnit: Serializable { byte, kilobyte, megabyte, gigabyte, terrabyte }
@Suppress("EnumEntryName")
enum class MemoryConsumptionKind: Serializable { RAM, disc }
data class Memory(
    var kind: MemoryConsumptionKind,
    var value: Long,
    var unit: MemoryUnit
): Resource {
    override fun toString(): String {
        return "consumed $value $unit of $kind"
    }
}

@Suppress("EnumEntryName")
enum class TimeUnit: Serializable {
    nanoseconds, microseconds, milliseconds, seconds, minutes;
}
class Time private constructor(private val value: Double, private val unit: TimeUnit): Resource {
    override fun toString(): String {
        return "took $value $unit"
    }

    companion object {
        fun ofNanos(nanos: Long): Time {
            return when {
                nanos > 60_000_000_000 -> Time(nanos.toDouble() / 60_000_000_000, TimeUnit.minutes)
                nanos > 1_000_000_000 -> Time(nanos.toDouble() / 1_000_000_000, TimeUnit.seconds)
                nanos > 1_000_000 -> Time(nanos.toDouble() / 1_000_000, TimeUnit.milliseconds)
                nanos > 1_000 -> Time(nanos.toDouble() / 1_000, TimeUnit.microseconds)
                nanos > 0 -> Time(nanos.toDouble(), TimeUnit.nanoseconds)
                else -> throw IllegalArgumentException("nanos must be non-negative")
            }
        }
    }
}
