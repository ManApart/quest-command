package traveling.location.location

import core.conditional.ConditionalStringBuilder
import core.properties.PropsBuilder
import explore.listen.SOUND_DESCRIPTION
import explore.listen.SOUND_LEVEL
import explore.listen.SOUND_LEVEL_DEFAULT
import traveling.scope.LIGHT

class LocationRecipeBuilder(val name: String) {
    private val propsBuilder = PropsBuilder()
    private val descriptionBuilder = ConditionalStringBuilder("")
    private val weatherBuilder = ConditionalStringBuilder("Still")
    private var material = "Void"
    private val slots = mutableListOf<String>()
    private val activatorBuilders = mutableListOf<LocationThingBuilder>()
    private val creatureBuilders = mutableListOf<LocationThingBuilder>()
    private val itemBuilders = mutableListOf<LocationThingBuilder>()
    private var baseNames = mutableListOf<String>()
    private var bases = mutableListOf<LocationRecipeBuilder>()


    fun buildWithBase(builders: Map<String, LocationRecipeBuilder>): LocationRecipe {
        val bases = this.bases + baseNames.map { builders[it]!! }
        return build(bases)
    }

    fun build(): LocationRecipe {
        val props = propsBuilder.build()
        val desc = descriptionBuilder.build()
        val weatherChoice = weatherBuilder.build()
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
            material = material,
            slots = slots,
            properties = props,
        )
    }

    fun build(bases: List<LocationRecipeBuilder>): LocationRecipe {
        val props = propsBuilder.build(bases.map { it.propsBuilder })
        val desc = descriptionBuilder.build(bases.map { it.descriptionBuilder })
        val weatherChoice = weatherBuilder.build(bases.map { it.weatherBuilder })
        val materialChoice = (listOf(material) +  bases.map { it.material }.reversed()).firstOrNull { it != "Void" } ?: "Void"
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
            material = materialChoice,
            slots = slots,
            properties = props,
        )
    }

    /**
     * Note that each time this function is used, the latter extends object will win any extension conflicts.
     * extends(tree) - fire health 2
     * extends(burnable) - fire health 1
     * The end thing will have fire health 1
     */
    fun extends(other: String) = baseNames.add(other)
    fun extends(other: LocationRecipeBuilder) = bases.add(other)

    fun props(initializer: PropsBuilder.() -> Unit) {
        propsBuilder.apply(initializer)
    }

    fun slot(vararg slot: String) = this.slots.addAll(slot.toList())

    fun description(description: String){
        this.descriptionBuilder.option(description)
    }

    fun description(initializer: ConditionalStringBuilder.() -> Unit): ConditionalStringBuilder {
        return descriptionBuilder.apply(initializer)
    }

    fun activator(name: String, x: Int = 0, y: Int = 0, z: Int = 0) {
        activatorBuilders.add(LocationThingBuilder(name).apply{vector(x,y,z)})
    }

    fun activator(name: String, initializer: LocationThingBuilder.() -> Unit = {}) {
        activatorBuilders.add(LocationThingBuilder(name).apply(initializer))
    }

    fun creature(name: String, x: Int = 0, y: Int = 0, z: Int = 0) {
        creatureBuilders.add(LocationThingBuilder(name).apply{vector(x,y,z)})
    }

    fun creature(name: String, initializer: LocationThingBuilder.() -> Unit = {}) {
        creatureBuilders.add(LocationThingBuilder(name).apply(initializer))
    }

    fun item(name: String, x: Int = 0, y: Int = 0, z: Int = 0) {
        itemBuilders.add(LocationThingBuilder(name).apply{vector(x,y,z)})
    }

    fun item(name: String, initializer: LocationThingBuilder.() -> Unit = {}) {
        itemBuilders.add(LocationThingBuilder(name).apply(initializer))
    }

    fun material(material: String){
        this.material = material
    }

    fun weather(weather: String){
        this.weatherBuilder.option(weather)
    }

    fun weather(initializer: ConditionalStringBuilder.() -> Unit): ConditionalStringBuilder {
        return weatherBuilder.apply(initializer)
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