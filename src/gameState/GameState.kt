package gameState

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object GameState {
    val player = Player()

    val world = loadLocations()

    private fun loadLocations(): Location {
        val path = "/resource/data/Locations.json"
        println("parsing resources $path")
        val json = this::class.java.classLoader.getResource("data/Locations.json").readText()
        return jacksonObjectMapper().readValue(json)
    }
}