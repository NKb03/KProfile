/**
 * @author Nikolaus Knop
 */

package nikok.kprofile.impl

import memento.Memento
import memento.Memorizer
import memento.SelfMemorable
import nikok.kprofile.api.ProfilingResultsNotFoundException
import nikok.kprofile.api.Resource
import nikok.kprofile.api.Tag
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

internal object Results {
    private val RESULTS_FOLDER = resultsPath()
    private fun resultsPath(): Path {
        val appData = System.getenv("LOCALAPPDATA")
        val appDataPath = Paths.get(appData)
        val resultPath = appDataPath.resolve("kprofile").resolve("results")
        Files.createDirectories(resultPath)
        return resultPath
    }

    fun get(topic: String, tags: List<Tag>): List<Result> {
        val bis = getInputStream(topic, tags)
        val results = readResults(bis)
        bis.close()
        return results
    }

    @Suppress("UNCHECKED_CAST")
    private fun readResults(input: InputStream): List<Result> {
        val memento = Memento.readFrom(input)
        val remembered = memorizer.remember(memento)
        return remembered as List<Result>
    }

    fun saveResults(topic: String, tags: List<Tag>, results: List<Result>) {
        val out = getOutputStream(topic, tags)
        val memento = memorizer.memorize(results)
        memento.writeTo(out)
    }

    private fun getPath(topic: String, tags: List<Tag>, mustExist: Boolean): Path {
        val fileName = tags.hashCode().toString()
        val root = Results.RESULTS_FOLDER
        val folderName = topic.replace(' ', '_')
        val path = Paths.get("$root\\$folderName\\$fileName")
        if (mustExist && !Files.exists(path)) {
            throw ProfilingResultsNotFoundException(topic, tags)
        }
        return path
    }

    private fun getInputStream(topic: String, tags: List<Tag>): InputStream {
        val path = getPath(topic, tags, mustExist = true)
        return Files.newInputStream(path).buffered()
    }

    private fun getOutputStream(topic: String, tags: List<Tag>): OutputStream {
        val path = getPath(topic, tags, mustExist = false)
        if (!Files.exists(path.parent)) {
            Files.createDirectory(path.parent)
        }
        if (!Files.exists(path)) {
            Files.createFile(path)
        }
        return Files.newOutputStream(path).buffered()
    }

    private val memorizer = Memorizer.newInstance()

}

internal data class Result(val description: String, val resourcesNeeded: List<Resource>) : SelfMemorable {
    private companion object {
        fun createAdapter() = Result("", emptyList())
    }
}