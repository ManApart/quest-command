package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.events.EventListener
import core.gameState.Body
import core.gameState.Item

object BodyManager {
    private val bodies = loadBodies()

    private fun loadBodies(): List<Body> {
        val json = this::class.java.classLoader.getResource("core/data/Bodies.json").readText()
        return jacksonObjectMapper().readValue(json)
    }

    fun bodyExists(name: String): Boolean {
        return bodies.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } != null
    }

    fun getBody(name: String): Body {
        return bodies.first { it.name.toLowerCase() == name.toLowerCase() }.copy()
    }
}