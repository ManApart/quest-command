package status.effects

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Effect

object EffectManager {
    private val effects = loadEffects()

//    class ItemSpawner : EventListener<SpawnItemEvent>() {
//        override fun execute(event: SpawnItemEvent) {
//            if (itemExists(event.itemName)) {
//                val item = getItem(event.itemName)
//                item.count = event.count
//                EventManager.postEvent(ItemSpawnedEvent(item, event.target))
//            } else {
//                println("Could not spawn ${event.itemName} because it could not be found.")
//            }
//        }
//    }

    private fun loadEffects(): List<Effect> {
        val json = this::class.java.classLoader.getResource("core/data/Effects.json").readText()
        return jacksonObjectMapper().readValue(json)
    }

    fun effectExists(name: String): Boolean {
        return effects.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } != null
    }

    fun effectExists(name: List<String>): Boolean {
        if (name.isEmpty()) return false

        val fullName = name.joinToString(" ").toLowerCase()
        return effects.firstOrNull { fullName.contains(it.name.toLowerCase()) } != null
    }

    fun getEffect(name: String): Effect {
        return effects.first { it.name.toLowerCase() == name.toLowerCase() }.copy()
    }

    fun getEffect(name: List<String>): Effect {
        val fullName = name.joinToString(" ").toLowerCase()
        return effects.first { fullName.contains(it.name.toLowerCase()) }.copy()
    }

    fun getEffects(names: List<String>): List<Effect> {
        return names.map { getEffect(it) }.toList()
    }
}