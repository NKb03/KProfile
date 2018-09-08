/**
 * @author Nikolaus Knop
 */

package nikok.kprofile.impl

import nikok.kprofile.api.Resource
import nikok.kprofile.api.ResultFormat
import nikok.kprofile.api.Tag
import nikok.kprofile.api.view
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Paths

internal object PlainText: ResultFormat {
    private fun List<Resource>.message(): String {
        return joinToString(separator = ", ")
    }

    override fun view(topic: String, tags: Array<out Tag>, out: Appendable) {
        val results = Results.get(topic, tags.asList())
        out.append("results for $topic: ")
        out.append("\n")
        for (r in results) {
            out.append(r.description)
            out.append(" ")
            out.append(r.resourcesNeeded.message())
            out.append("\n")
        }
    }

    internal fun view(fileName: String, topic: String, tags: Array<out Tag>) {
        val p = Paths.get(fileName)
        Files.createFile(p)
        val os = Files.newOutputStream(p)
        val ps = PrintStream(os)
        view(topic, *tags, out = ps)
    }

    override fun diff(topic: String, tagss: Array<out List<Tag>>, out: Appendable) {
        val resultss = tagss.associate { tags ->
            tags to Results.get(topic, tags).associate { res -> res.description to res.resourcesNeeded }
        }
        val descriptions = resultss.flatMapTo(mutableSetOf()) { (_, descriptionToResults) ->
            descriptionToResults.map { (description, _) -> description }
        }
        out.append("$topic: ")
        out.append("\n\n")
        for (desc in descriptions) {
            out.append("$desc: ")
            out.append("\n")
            for (tags in tagss) {
                out.append(" ".repeat(4))
                val result = resultss[tags]!![desc]!!
                val tagsMsg = tags.joinToString(separator = ", ", transform = Tag::name)
                out.append(tagsMsg)
                out.append(": ")
                val resultMsg = result.message()
                out.append(resultMsg)
                out.append("\n")
            }
        }
    }
}