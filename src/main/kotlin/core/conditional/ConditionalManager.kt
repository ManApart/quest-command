package core.conditional

import core.DependencyInjector

//Eventually enum of what type to point to  (dialogue, description, weather, etc)
object ConditionalManager {
    private var parser = DependencyInjector.getImplementation(WeatherStringsCollection::class.java)
    private var conditionalStrings = parser.values

    fun reset() {
        parser = DependencyInjector.getImplementation(WeatherStringsCollection::class.java)
        conditionalStrings = parser.values
    }

    fun getConditionalString(name: String, type: ConditionalStringType) : ConditionalString {
        return conditionalStrings.first { it.name == name }
    }

    fun getOption(name: String, type: ConditionalStringType) : String {
        return getConditionalString(name, type).getOption()
    }

}