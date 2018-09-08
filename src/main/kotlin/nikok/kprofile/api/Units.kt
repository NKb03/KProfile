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
enum class TimeUnit: Serializable { nanoSeconds, milliseconds, seconds, minutes }
data class Time(var value: Long, var unit: TimeUnit): Resource {
    override fun toString(): String {
        return "took $value $unit"
    }
}

val Long.nanos get() = Time(this, TimeUnit.nanoSeconds)
val Long.millis get() = Time(this, TimeUnit.milliseconds)