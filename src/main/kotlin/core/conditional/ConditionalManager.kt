package core.conditional

import core.DependencyInjector
import traveling.location.location.LocationDescriptionsCollection
import traveling.location.weather.WeatherStringsCollection
import core.conditional.ConditionalStringType.*

object ConditionalManager {
    private var types = buildTypeMap()

    private fun buildTypeMap() : Map<ConditionalStringType, List<ConditionalString>>{
        return mapOf(
                DEFAULT to listOf(),
                LOCATION_DESCRIPTION to DependencyInjector.getImplementation(LocationDescriptionsCollection::class.java).values,
                WEATHER to DependencyInjector.getImplementation(WeatherStringsCollection::class.java).values
        )
    }

    fun reset() {
        types = buildTypeMap()
    }

    fun getTypeMap() : Map<ConditionalStringType, List<ConditionalString>> {
        return types
    }

    fun getConditionalString(name: String, type: ConditionalStringType) : ConditionalString {
        return (types[type] ?: error("Unknown Condition Type $type")).first { it.name == name }
    }

    fun getOption(name: String, type: ConditionalStringType) : String {
        return getConditionalString(name, type).getOption()
    }

}