/**
 * @author Nikolaus Knop
 */

package nikok.kprofile.api

import nikok.kprofile.impl.MarkdownFormat
import nikok.kprofile.impl.PlainText

interface ResultFormat {
    fun diff(topic: String, out: Appendable, vararg tagss: List<Tag>)

    fun view(topic: String, tags: List<Tag>, out: Appendable)
}

val markdown: ResultFormat = MarkdownFormat

val plainText: ResultFormat = PlainText