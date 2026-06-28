package building

import readConfig
import java.io.File

fun main() {
    val config = readConfig()
    val keywords = config.fancyReplace.keywords
    File(".").listFilesRecursive { f -> f.name.startsWith(".") }.filter { it.name.endsWith("kt") }
        .also { println("Processing ${it.size} kt files") }
        .forEach { file ->
            val contents = keywords.fold(file.readText()) { acc, keyword ->
                val oldVal = "material(\"$keyword\")"
                val newVal = "material(${keyword.uppercase()})"
                acc.replace(oldVal, newVal)
            }
            if (config.dryRun) {
                println(contents)
            } else file.writeText(contents)
        }
}


private fun File.listFilesRecursive(exclude: (File) -> Boolean): List<File> {
    val (folders, files) = listFiles()!!.filter { !exclude(it) }.partition { it.isDirectory }
    return files + folders.flatMap { it.listFilesRecursive(exclude) }
}
