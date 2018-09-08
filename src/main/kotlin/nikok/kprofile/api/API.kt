/**
 * @author Nikolaus Knop
 */

package nikok.kprofile.api
import nikok.kprofile.impl.ProfileBodyImpl
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Files.createFile
import java.nio.file.Files.exists
import java.nio.file.Paths

fun profile(topic: String, vararg tags: Tag, body: ProfileBody.() -> Unit) {
    val pb = ProfileBodyImpl(topic, tags.asList())
    pb.body()
    pb.write()
}

abstract class ProfilingSuite(topic: String, vararg tags: Tag, body: ProfileBody.() -> Unit) {
    init {
        val pb = ProfileBodyImpl(topic, tags.asList())
        pb.body()
        pb.write()
    }
}

fun view(topic: String, vararg tags: Tag, out: PrintStream = System.out) {
    plainText.view(topic, *tags, out = out)
}

fun view(topic: String, vararg tags: Tag, fileName: String) {
    markdown().view(topic, tags = *tags, fileName = fileName)
}

fun diff(topic: String, vararg tagss: List<Tag>, out: PrintStream = System.out) {
    plainText.diff(topic, *tagss, out = out)
}

fun ResultFormat.view(topic: String, vararg tags: Tag, fileName: String) {
    val path = Paths.get(fileName)
    if (!exists(path)) createFile(path)
    val out = Files.newOutputStream(path).bufferedWriter()
    view(topic, *tags, out = out)
    out.close()
}



