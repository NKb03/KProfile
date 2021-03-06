/**
 *@author Nikolaus Knop
 */

package nikok.kprofile.impl

import nikok.kprofile.api.*

internal class ProfileBodyImpl(
    private val topic: String, private val tags: List<Tag>
): ProfileBody {
    private val results = mutableListOf<Result>()

    private fun addResult(result: Result) {
        results.add(result)
    }

    override infix fun String.took(time: Time) {
        addResult(Result(this, listOf(time)))
    }

    override infix fun String.consumed(memory: Memory) {
        addResult(Result(this, listOf(memory)))
    }

    override fun String.needed(vararg resources: Resource) {
        addResult(Result(this, resources.asList()))
    }

    internal fun write() {
        Results.saveResults(topic, tags, results)
    }

    override fun <T> profile(description: String, action: () -> T): T {
        val before = System.nanoTime()
        val t = action.invoke()
        val after = System.nanoTime()
        val nanos = after - before
        val time = Time.ofNanos(nanos)
        description took time
        return t
    }
}

