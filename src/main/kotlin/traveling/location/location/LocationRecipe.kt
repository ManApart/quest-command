package traveling.location.location

import core.conditional.ConditionalString
import core.properties.Properties
import core.utility.Named

val NOWHERE = LocationRecipe("Nowhere")

const val HEAT = "Heat"
const val LIGHT = "Light"

//TODO - combine items and equipped items
//only do equip check if slots exist
data class LocationRecipe(
    override val name: String,
    private val description: ConditionalString = ConditionalString(""),
    val activators: List<LocationTarget> = listOf(),
    val creatures: List<LocationTarget> = listOf(),
    val items: List<LocationTarget> = listOf(),
    val weatherChangeFrequency: Int = 5,
    val weather: ConditionalString = ConditionalString("Still"),
    val properties: Properties = Properties(),
    val slots: List<String> = listOf()
) : Named {
    constructor(base: LocationRecipe) : this(
            base.name,
            base.description,
            base.activators.toList(),
            base.creatures.toList(),
            base.items.toList(),
            base.weatherChangeFrequency,
            base.weather,
            Properties(base.properties),
            base.slots
    )

    override fun toString(): String {
        return name
    }

    fun getWeatherName() : String {
        return weather.getOption()
    }

    fun getDescription(): String {
        return description.getOption()
    }

}