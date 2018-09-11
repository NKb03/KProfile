/**
 * @author Nikolaus Knop
 */

package nikok.kprofile.api

import memento.SelfMemorable

interface Resource: SelfMemorable

@Suppress("EnumEntryName")
enum class MemoryUnit { byte, kilobyte, megabyte, gigabyte, terrabyte }
@Suppress("EnumEntryName")
enum class MemoryConsumptionKind { RAM, disc }
class Memory internal constructor(
    var kind: MemoryConsumptionKind,
    var value: Double,
    var unit: MemoryUnit
): Resource {
    override fun toString(): String {
        return "consumed $value $unit of $kind"
    }
}

private const val KILO_BYTE = 1024L
private const val MEGA_BYTE = KILO_BYTE * 1024
private const val GIGA_BYTE = MEGA_BYTE * 1024
private const val TERRA_BYTE = GIGA_BYTE * 1024


fun Long.of(kind: MemoryConsumptionKind): Memory {
    return when {
        this > TERRA_BYTE -> Memory(kind, toDouble() / TERRA_BYTE, MemoryUnit.terrabyte)
        this > GIGA_BYTE -> Memory(kind, toDouble() / GIGA_BYTE, MemoryUnit.gigabyte)
        this > MEGA_BYTE -> Memory(kind, toDouble() / MEGA_BYTE, MemoryUnit.megabyte)
        this > KILO_BYTE -> Memory(kind, toDouble() / KILO_BYTE, MemoryUnit.kilobyte)
        this > 0 -> Memory(kind, toDouble(), MemoryUnit.byte)
        else -> throw IllegalArgumentException("Cannot accept negative memory consumption")
    }
}

@Suppress("EnumEntryName")
enum class TimeUnit {
    nanoseconds, microseconds, milliseconds, seconds, minutes;
}
class Time private constructor(private val value: Double, private val unit: TimeUnit): Resource {
    override fun toString(): String {
        return "took $value $unit"
    }

    companion object {
        private const val MICRO = 1000L
        private const val MILLI = MICRO * 1000
        private const val SECOND = MILLI * 1000
        private const val MINUTE = SECOND * 60

        fun ofNanos(nanos: Long): Time {
            return when {
                nanos > MINUTE -> Time(nanos.toDouble() / MINUTE, TimeUnit.minutes)
                nanos > SECOND -> Time(nanos.toDouble() / SECOND, TimeUnit.seconds)
                nanos > MILLI -> Time(nanos.toDouble() / MILLI, TimeUnit.milliseconds)
                nanos > MICRO -> Time(nanos.toDouble() / MICRO, TimeUnit.microseconds)
                nanos >= 0 -> Time(nanos.toDouble(), TimeUnit.nanoseconds)
                else -> throw IllegalArgumentException("Cannot accept negative time")
            }
        }
    }
}
