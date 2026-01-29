package system.help

import java.nio.charset.StandardCharsets


actual suspend fun getVersion(): String {
    val cl = Thread.currentThread().contextClassLoader ?: ViewVersion::class.java.classLoader
    return cl.getResourceAsStream("version.txt")?.use { input ->
        return input.bufferedReader(StandardCharsets.UTF_8).readText().trim()
    } ?: ""
}