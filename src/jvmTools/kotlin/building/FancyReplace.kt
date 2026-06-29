package building

import FancyReplaceConfig
import readConfig
import java.io.File

fun main() {
    val config = readConfig()
    File(".").listFilesRecursive { f -> f.name.startsWith(".") }.filter { it.name.endsWith("kt") }
        .also { println("Processing ${it.size} kt files") }
        .forEach { file ->
            val initial = file.readText()
            val contents = config.fancyReplace.singleParamFunction(initial)
            if (initial != contents) {
                if (config.dryRun) {
                    println(file.name)
                    println(contents + "\n")
                } else file.writeText(contents)
            }
        }
}

private fun File.listFilesRecursive(exclude: (File) -> Boolean): List<File> {
    val (folders, files) = listFiles()!!.filter { !exclude(it) }.partition { it.isDirectory }
    return files + folders.flatMap { it.listFilesRecursive(exclude) }
}

private fun FancyReplaceConfig.singleParamFunction(initial: String): String {
    return keywords.fold(initial) { acc, keyword ->
        val oldVal = "${functionName}(\"$keyword\")"
        val newVal = "${functionName}(${keyword.uppercase()})"
        acc.replace(oldVal, newVal)
    }
}
