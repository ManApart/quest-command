package system

import crafting.RecipeJsonParser
import crafting.RecipeParser
import system.location.LocationJsonParser
import system.location.LocationParser

object DependencyInjector {
    private val interfaces = mutableMapOf<Class<*>, Any>()

    init {
        setDefaultImplementations()
    }

    fun <T : E, E: Any> setImplementation(targetInterface: Class<E>, implementation: T) {
        interfaces[targetInterface] = implementation
    }

    fun <T : E, E : Any> getImplementation(targetInterface: Class<E>): T {
        return interfaces[targetInterface] as T
    }

    private fun setDefaultImplementations(){
        setImplementation(LocationParser::class.java, LocationJsonParser())
        setImplementation(RecipeParser::class.java, RecipeJsonParser())
    }


}