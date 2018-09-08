/**
 * @author Nikolaus Knop
 */

package nikok.kprofile.cmd

import nikok.kprofile.api.ProfilingResultsNotFoundException
import nikok.kprofile.api.Tag
import nikok.kprofile.api.diff
import nikok.kprofile.api.view
import java.io.InputStream
import java.io.Reader
import java.nio.file.InvalidPathException
import java.util.*
private object KProfile {
    private const val VERSION = "1.0.0"

    private const val ART = """
|------------------------------------------------------------------------------------------|
|                                                                                          |
|   *     *    * * * * *   * * * * *   * * * * *   * * * * *   *   *           * * * * *   |
|   *   *      *       *   *       *   *       *   *           *   *           *           |
|   * *        *       *   *       *   *       *   *           *   *           *           |
|   *          * * * * *   * * * * *   *       *   * * * * *   *   *           * * * * *   |
|   * *        *           * *         *       *   *           *   *           *           |
|   *   *      *           *    *      *       *   *           *   *           *           |
|   *     *    *           *       *   * * * * *   *           *   * * * * *   * * * * *   |
|                                                                                          |
|------------------------------------------------------------------------------------------|
"""

    private val LINE_SEPARATOR = "-".repeat(100)

    @JvmStatic fun main(args: Array<String>) {
        if (args.isEmpty()) {
            startInteractive()
        } else {
            execute(args.asList())
        }
    }

    private fun startInteractive() {
        println(ART)
        Thread.sleep(10)
        while (true) {
            val console = System.console() ?: return noConsole()
            print("> ")
            val line = console.readLine()
            val tokens = line.split(WHITE_SPACE)
            if (execute(tokens)) return
        }
    }

    private fun noConsole() {
        println("No console")
    }

    private fun execute(words: List<String>): Boolean {
        val (name, args) = words.extractCommand() ?: return false
        return execute(name, args)
    }

    private val WHITE_SPACE = Regex("\\s+")

    private fun InputStream.nextWords(): List<String> {
        return Scanner(this).use { r ->
            val text = r.nextLine()
            text.split(WHITE_SPACE)
        }
    }

    private fun Reader.readAvailable(): String {
        return buildString {
            while (ready()) {
                append(read().toChar())
            }
        }
    }

    private fun List<String>.extractCommand(): Pair<String, List<String>>? {
        val name = firstOrNull() ?: return null
        val args = drop(1)
        return name to args
    }

    private fun execute(command: String, args: List<String>): Boolean {
        when (command) {
            "version" -> printVersion()
            "help" -> printHelp()
            "view" -> viewResults(args)
            "diff" -> diff(args)
            "exit" -> return true
            else -> unknownCommand(command)
        }
        return false
    }

    private fun printVersion() {
        println("Version: $VERSION")
    }

    private fun printDiffUsage() {
        println("usage: view <topic> '<tag1 tag2 tagN>' '<tag1 tag2 tagN>' ...")
    }

    private fun diff(args: List<String>) {
        val topic = args.getOrElse(0) {
            printDiffUsage()
            return
        }
        val tagss = args.drop(1).map { tags -> tags.split(' ').map { tagName -> Tag.create(tagName) } }
        if (tagss.isEmpty()) {
            printDiffUsage()
            return
        }
        if (tagss.any { tags -> tags.isEmpty() }) {
            printDiffUsage()
            return
        }
        try {
            diff(topic, *tagss.toTypedArray())
        } catch (e: ProfilingResultsNotFoundException) {
            println(e.message)
        }
    }

    private fun printViewUsage() {
        println("usage: view <topic> <tag1 tag2 tagN>")
    }

    private fun viewResults(args: List<String>) {
        val topic = args.getOrElse(0) {
            println("no topic")
            printViewUsage()
            return
        }
        val tags = args.drop(1).map { tagName -> Tag.create(tagName) }
        if (tags.isEmpty()) {
            println("no tags")
            printViewUsage()
            return
        }
        try {
            view(topic, *tags.toTypedArray())
        } catch (notFound: ProfilingResultsNotFoundException) {
            println(notFound.message)
        } catch (ipe: InvalidPathException) {
            println("Invalid topic name $topic")
        }
    }

    private fun printHelp() {
        println(LINE_SEPARATOR)
        explainHelp()
        println(LINE_SEPARATOR)
        explainVersion()
        println(LINE_SEPARATOR)
        explainView()
        println(LINE_SEPARATOR)
        explainDiff()
        println(LINE_SEPARATOR)
    }

    private fun explainVersion() {
        println("'version': prints the version of your distribution of KProfile")
    }

    private fun explainDiff() {
        println("'diff': ")
        printDiffUsage()
        println("Compares the profiling results for the specified topics with the specified tags")
    }

    private fun explainHelp() {
        println("'help': lists all commands")
    }

    private fun explainView() {
        println("'view': ")
        printViewUsage()
        println("Lists the profiling results for the specified topic with the specified tags")
    }

    private fun unknownCommand(next: String) {
        println(LINE_SEPARATOR)
        println("Unknown command '$next'")
        println("For usage explanation use 'help'")
        println(LINE_SEPARATOR)
    }
}