package system

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
            interfaces[targetInterface] as T
        } else {
            defaultImplementations[targetInterface] as T
        }
    }

    private fun createDefaultImplementations(): Map<Class<*>, Any> {
        return mapOf(
                LocationParser::class.java to LocationJsonParser(),
                RecipeParser::class.java to RecipeJsonParser(),
                ItemParser::class.java to ItemJsonParser()
        )
    }


}