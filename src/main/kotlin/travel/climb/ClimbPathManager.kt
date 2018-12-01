package travel.climb

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.climb.ClimbPath
import core.utility.JsonDirectoryParser

object ClimbPathManager {
    private val paths = JsonDirectoryParser.parseDirectory("/data/content/climb-paths", ::parseFile)
    private fun parseFile(path: String): List<ClimbPath> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    fun getPath(name: String): ClimbPath {
        return paths.first { it.name.toLowerCase() == name.toLowerCase() }
    }
    fun pathExists(name: String): Boolean {
        return paths.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } != null
    }
}