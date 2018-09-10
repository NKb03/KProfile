/**
 * @author Nikolaus Knop
 */

package nikok.kprofile.api
import nikok.kprofile.impl.ProfileBodyImpl
import java.nio.file.Files
import java.nio.file.Files.createFile
import java.nio.file.Files.exists
import java.nio.file.Path
import java.nio.file.Paths

fun profile(topic: String, tags: List<Tag>, body: ProfileBody.() -> Unit) {
    val pb = ProfileBodyImpl(topic, tags)
    pb.body()
    pb.write()
}

fun ResultFormat.view(topic: String, tags: List<Tag>, path: Path) {
    if (!exists(path)) createFile(path)
    val out = Files.newOutputStream(path).bufferedWriter()
    view(topic, tags, out)
    out.close()
}

fun ResultFormat.view(topic: String, tags: List<Tag>) {
    view(topic, tags, System.out)
    System.out.flush()
}

fun view(topic: String, tags: List<Tag>) {
    plainText.view(topic, tags)
}


fun ResultFormat.diff(topic: String, path: Path, vararg tags: List<Tag>) {
    val out = Files.newBufferedWriter(path)
    diff(topic, out, *tags)
    out.close()
}

fun ResultFormat.diff(topic: String, vararg tags: List<Tag>) {
    diff(topic, System.out, *tags)
}

fun diff(topic: String, vararg tags: List<Tag>) {
    plainText.diff(topic, *tags)
}



