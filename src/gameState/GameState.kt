package gameState

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object GameState {
    val world = loadLocations()
    val player = Player()
    private val items = loadItems()

    private fun loadLocations(): Location {
        val json = this::class.java.classLoader.getResource("data/Locations.json").readText()
        return jacksonObjectMapper().readValue(json)
    }

    private fun loadItems(): List<Item> {
        val json = this::class.java.classLoader.getResource("data/Items.json").readText()
        return jacksonObjectMapper().readValue(json)
    }

    fun getItem(name: String) : Item{
        return items.first { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun itemExists(name: String) : Boolean{
        return items.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } != null
    }

}