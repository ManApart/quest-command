package building

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun main() {
    generateVersion()
}

//TODO - tag + commit
fun generateVersion(){
    val version = File(".").runCommand("git rev-parse HEAD")!!.split("\n").first()
    File("src/commonMain/resources/version.txt").writeText(version)
}

private fun File.runCommand(command: String): String? {
    val parts = command.split("\\s".toRegex())
    return try {
        val proc = ProcessBuilder(*parts.toTypedArray())
            .directory(this)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .redirectErrorStream(true)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        proc.inputStream.bufferedReader().readText()
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}