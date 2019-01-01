package system

import core.gameState.quests.QuestJsonParser
import core.gameState.quests.QuestParser
import crafting.RecipeJsonParser
import crafting.RecipeParser
import system.location.LocationJsonParser
import system.location.LocationParser

object DependencyInjector {
    private val interfaces = mutableMapOf<Class<*>, Any>()
    private val defaultImplementations = createDefaultImplementations()


    fun <T : E, E : Any> setImplementation(targetInterface: Class<E>, implementation: T) {
        interfaces[targetInterface] = implementation
    }

    fun <T : E, E : Any> getImplementation(targetInterface: Class<E>): T {
        return if (interfaces.containsKey(targetInterface)) {
            @Suppress("UNCHECKED_CAST")
            interfaces[targetInterface] as T
        } else {
            @Suppress("UNCHECKED_CAST")
            defaultImplementations[targetInterface] as T
        }
    }

    private fun createDefaultImplementations(): Map<Class<*>, Any> {
        return mapOf(
                ActivatorParser::class.java to ActivatorJsonParser(),
                ItemParser::class.java to ItemJsonParser(),
                LocationParser::class.java to LocationJsonParser(),
                QuestParser::class.java to QuestJsonParser(),
                RecipeParser::class.java to RecipeJsonParser()
        )
    }


}