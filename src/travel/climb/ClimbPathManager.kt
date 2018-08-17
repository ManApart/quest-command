package travel.climb

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.climb.ClimbPath

object ClimbPathManager {
    private val paths = loadPaths()

    private fun loadPaths(): List<ClimbPath> {
        val json = this::class.java.getResourceAsStream("/resource/data/ClimbPaths.json")
        return jacksonObjectMapper().readValue(json)
    }

    fun getPath(name: String): ClimbPath {
        return paths.first { it.name.toLowerCase() == name.toLowerCase() }
    }
}