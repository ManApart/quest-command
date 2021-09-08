package traveling.location.location

import core.conditional.ConditionalStringPointer
import core.conditional.ConditionalStringType
import core.properties.PropsBuilder

class LocationRecipeBuilder(val name: String) {
    private var propsBuilder = PropsBuilder()
    private val slots = mutableListOf<String>()
    private var description: ConditionalStringPointer? = null
    private var weather: ConditionalStringPointer? = null
    private val activatorBuilders = mutableListOf<LocationTargetBuilder>()
    private val creatureBuilders = mutableListOf<LocationTargetBuilder>()
    private val itemBuilders = mutableListOf<LocationTargetBuilder>()

    internal fun build(): LocationRecipe {
        val props = propsBuilder.build()

        return LocationRecipe(
            name,
            slots = slots,
            properties = props,
        )
    }

    fun extends(other: String){

    }

    fun props(initializer: PropsBuilder.() -> Unit) {
        propsBuilder.apply(initializer)
    }

    fun slot(vararg slot: String) = this.slots.addAll(slot.toList())

    fun descriptionPointer(description: String){
        this.description = ConditionalStringPointer(description, ConditionalStringType.LOCATION_DESCRIPTION)
    }

    fun description(description: String){
        this.description = ConditionalStringPointer(description)
    }

    fun activator(name: String, x: Int = 0, y: Int = 0, z: Int = 0) {
        activatorBuilders.add(LocationTargetBuilder(name).apply{vector(x,y,z)})
    }

    fun activator(name: String, initializer: LocationTargetBuilder.() -> Unit = {}) {
        activatorBuilders.add(LocationTargetBuilder(name).apply(initializer))
    }

    fun creature(name: String, x: Int = 0, y: Int = 0, z: Int = 0) {
        creatureBuilders.add(LocationTargetBuilder(name).apply{vector(x,y,z)})
    }

    fun creature(name: String, initializer: LocationTargetBuilder.() -> Unit = {}) {
        creatureBuilders.add(LocationTargetBuilder(name).apply(initializer))
    }

    fun item(name: String, x: Int = 0, y: Int = 0, z: Int = 0) {
        itemBuilders.add(LocationTargetBuilder(name).apply{vector(x,y,z)})
    }

    fun item(name: String, initializer: LocationTargetBuilder.() -> Unit = {}) {
        itemBuilders.add(LocationTargetBuilder(name).apply(initializer))
    }

    fun weather(weather: String){
        this.weather = ConditionalStringPointer(weather, ConditionalStringType.WEATHER)
    }

}

fun locationRecipe(name: String, initializer: LocationRecipeBuilder.() -> Unit): LocationRecipeBuilder {
    return LocationRecipeBuilder(name).apply(initializer)
}