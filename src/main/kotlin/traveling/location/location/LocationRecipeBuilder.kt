package traveling.location.location

import core.conditional.ConditionalStringPointer
import core.conditional.ConditionalStringType
import core.properties.PropsBuilder
import explore.listen.SOUND_DESCRIPTION
import explore.listen.SOUND_LEVEL
import explore.listen.SOUND_LEVEL_DEFAULT

class LocationRecipeBuilder(val name: String) {
    private var propsBuilder = PropsBuilder()
    private val slots = mutableListOf<String>()
    private var description: ConditionalStringPointer? = null
    private var weather: ConditionalStringPointer? = null
    private val activatorBuilders = mutableListOf<LocationTargetBuilder>()
    private val creatureBuilders = mutableListOf<LocationTargetBuilder>()
    private val itemBuilders = mutableListOf<LocationTargetBuilder>()
    private var baseNames = mutableListOf<String>()
    private var bases = mutableListOf<LocationRecipeBuilder>()


    fun buildWithBase(builders: Map<String, LocationRecipeBuilder>): LocationRecipe {
        val bases = this.bases + baseNames.map { builders[it]!! }
        return build(bases)
    }

    fun build(): LocationRecipe {
        val props = propsBuilder.build()
        val desc = description ?: ConditionalStringPointer("")
        val weatherChoice = weather ?: ConditionalStringPointer("Still")
        val activators = activatorBuilders.build()
        val creatures = creatureBuilders.build()
        val items = itemBuilders.build()

        return LocationRecipe(
            name,
            desc,
            activators,
            creatures,
            items,
            weather = weatherChoice,
            slots = slots,
            properties = props,
        )
    }

    fun build(bases: List<LocationRecipeBuilder>): LocationRecipe {
        val basesR = bases.reversed()
        val props = propsBuilder.build(bases.map { it.propsBuilder })

        val desc = description ?: basesR.firstNotNullOfOrNull { it.description } ?: ConditionalStringPointer(name)
        val weatherChoice = weather ?: basesR.firstNotNullOfOrNull { it.weather } ?: ConditionalStringPointer(name)
        val allActivators = activatorBuilders.build() + bases.flatMap { it.activatorBuilders.build() }
        val allCreatures = creatureBuilders.build() + bases.flatMap { it.creatureBuilders.build() }
        val allItems = itemBuilders.build() + bases.flatMap { it.itemBuilders.build() }

        return LocationRecipe(
            name,
            desc,
            allActivators,
            allCreatures,
            allItems,
            weather = weatherChoice,
            slots = slots,
            properties = props,
        )
    }

    /**
     * Note that each time this function is used, the latter extends object will win any extension conflicts.
     * extends(tree) - fire health 2
     * extends(burnable) - fire health 1
     * The end target will have fire health 1
     */
    fun extends(other: String) = baseNames.add(other)
    fun extends(other: LocationRecipeBuilder) = bases.add(other)

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

    fun lightLevel(level: Int){
        this.propsBuilder.value(LIGHT, level)
    }

    fun sound(description: String){
        sound(SOUND_LEVEL_DEFAULT, description)
    }

    fun sound(level: Int, description: String){
        propsBuilder.value(SOUND_DESCRIPTION, description)
        propsBuilder.value(SOUND_LEVEL, level)
    }

}

fun locationRecipe(name: String, initializer: LocationRecipeBuilder.() -> Unit): LocationRecipeBuilder {
    return LocationRecipeBuilder(name).apply(initializer)
}