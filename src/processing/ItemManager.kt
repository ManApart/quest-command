package processing

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import gameState.Item

object ItemManager {
    private val items = loadItems()

    private fun loadItems(): List<Item> {
        val json = this::class.java.classLoader.getResource("data/Items.json").readText()
        return jacksonObjectMapper().readValue(json)
    }

    fun itemExists(name: String) : Boolean{
        return items.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } != null
    }

    fun itemExists(name: List<String>) : Boolean{
        if (name.isEmpty()) return false

        val fullName = name.joinToString(" ").toLowerCase()
        return items.firstOrNull { fullName.contains(it.name.toLowerCase()) } != null
    }

    fun getItem(name: String) : Item {
        return items.first { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getItem(name: List<String>) : Item {
        val fullName = name.joinToString(" ").toLowerCase()
        return items.first { fullName.contains(it.name.toLowerCase()) }
    }
}