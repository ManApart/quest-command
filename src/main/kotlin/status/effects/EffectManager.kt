package status.effects

import core.gameState.Effect
import core.utility.NameSearchableList
import system.DependencyInjector

object EffectManager {
    private var effects = loadEffects()

    fun reset() {
        effects = loadEffects()
    }

    private fun loadEffects(): NameSearchableList<Effect> {
        val parser = DependencyInjector.getImplementation(EffectParser::class.java)
        return parser.loadEffects()
    }

//    class SpawnItem : EventListener<SpawnItemEvent>() {
//        override fun execute(event: SpawnItemEvent) {
//            if (exists(event.itemName)) {
//                val item = getItem(event.itemName)
//                item.count = event.count
//                EventManager.postEvent(ItemSpawnedEvent(item, event.target))
//            } else {
//                display("Could not spawn ${event.itemName} because it could not be found.")
//            }
//        }
//    }


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
        return names.asSequence().map { getEffect(it) }.toList()
    }
}