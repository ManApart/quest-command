package core.gameState

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object GameState {
    val world = loadLocations()
    val player = Player()

    private fun loadLocations(): Location {
        val json = this::class.java.classLoader.getResource("core/data/Locations.json").readText()
        return jacksonObjectMapper().readValue(json)
    }

}