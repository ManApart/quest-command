package building

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun main() {
    generateVersion()
}

fun generateVersion(){
    val version = File(".").runCommand("git rev-parse HEAD")!!.split("\n").first()
    val versionText = """
        package system.help
        
        const val APP_VERSION = "$version"
        """.trimIndent()
    File("src/commonMain/kotlin/system/help/VersionHolder.kt").writeText(versionText)
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