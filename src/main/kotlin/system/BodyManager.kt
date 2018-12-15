package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Body
import core.utility.JsonDirectoryParser

object BodyManager {
    private val bodies = JsonDirectoryParser.parseDirectory("/data/generated/content/bodies", ::parseFile)
    private fun parseFile(path: String): List<Body> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    fun bodyExists(name: String): Boolean {
        return bodies.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } != null
    }

    fun getBody(name: String): Body {
        return bodies.first { it.name.toLowerCase() == name.toLowerCase() }.copy()
    }
}