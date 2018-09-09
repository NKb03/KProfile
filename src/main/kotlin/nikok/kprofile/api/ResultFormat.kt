/**
 * @author Nikolaus Knop
 */

package nikok.kprofile.api

import khtml.api.md.Darkula
import khtml.api.md.MarkdownTheme
import nikok.kprofile.impl.MarkdownFormat
import nikok.kprofile.impl.PlainText

interface ResultFormat {
    fun diff(topic: String, out: Appendable, vararg tagss: List<Tag>)

    fun view(topic: String, tags: List<Tag>, out: Appendable)
}

fun markdown(theme: MarkdownTheme = Darkula): ResultFormat = MarkdownFormat(theme)

val plainText: ResultFormat = PlainText