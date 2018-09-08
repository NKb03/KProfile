/**
 * @author Nikolaus Knop
 */

package nikok.kprofile.api

interface ProfileBody {
    infix fun String.took(time: Time)

    infix fun String.consumed(memory: Memory)

    fun String.needed(vararg resources: Resource)

    fun profile(description: String, action: () -> Unit)

    fun profileNanos(description: String, action: () -> Unit)
}