package core.conditional

import core.DependencyInjector
import core.conditional.ConditionalStringType.*
import traveling.location.location.LocationDescriptionsCollection
import traveling.location.weather.WeatherStringsCollection

object ConditionalManager {
    private var types = buildTypeMap()

    private fun buildTypeMap() : Map<ConditionalStringType, List<ConditionalString>>{
        return mapOf(
                DEFAULT to listOf(),
                LOCATION_DESCRIPTION to DependencyInjector.getImplementation(LocationDescriptionsCollection::class).values,
                WEATHER to DependencyInjector.getImplementation(WeatherStringsCollection::class).values
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