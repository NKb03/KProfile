package nikok.kprofile.impl

import khtml.api.*
import khtml.api.md.Markdown
import khtml.api.md.MarkdownTheme
import khtml.api.md.markdown
import nikok.kprofile.api.markdown
import nikok.kprofile.api.ResultFormat
import nikok.kprofile.api.Tag
import java.nio.file.Files
import java.nio.file.Paths

class MarkdownFormat(private val theme: MarkdownTheme) : ResultFormat {
    override fun diff(topic: String, vararg tagss: List<Tag>, out: Appendable) {
        val resultss = tagss.associate { tags ->
            tags to Results.get(topic, tags).associate { res -> res.description to res.resourcesNeeded }
        }
        val descriptions = resultss.flatMapTo(mutableSetOf()) { (_, descriptionToResults) ->
            descriptionToResults.map { (description, _) -> description }
        }
        markdown(out) {
            h1(topic)
            for (desc in descriptions) {
                h5(desc)
                for (results in resultss) {
                    val (tags, descToResults) = results
                    val resultsForTags = descToResults[desc]
                    p {
                        append(tags.joinToString { it.name })
                        append(": ")
                        if (resultsForTags == null) append("No results")
                        else append(resultsForTags.joinToString())
                    }
                }
            }
        }
    }

    override fun view(topic: String, vararg tags: Tag, out: Appendable) {
        markdown(out, theme) {
            val results = Results.get(topic, tags.asList())
            h(1, topic)
            h(2, tags.joinToString { it.name })
            ul {
                for (r in results) {
                    p {
                        append(r.description)
                        append(" ")
                        append(r.resourcesNeeded.joinToString())
                    }
                }
            }
        }
    }
}