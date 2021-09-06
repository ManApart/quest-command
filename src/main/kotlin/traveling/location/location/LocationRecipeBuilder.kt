package traveling.location.location

import core.properties.PropsBuilder

class LocationRecipeBuilder(val name: String) {
    private var propsBuilder = PropsBuilder()
    private val slots = mutableListOf<String>()

    internal fun build(): LocationRecipe {
        val props = propsBuilder.build()

        return LocationRecipe(
            name,
            slots = slots,
            properties = props,
        )
    }

    fun props(initializer: PropsBuilder.() -> Unit) {
        propsBuilder.apply(initializer)
    }

    fun slot(vararg slot: String) = this.slots.addAll(slot.toList())

}

fun locationRecipe(name: String, initializer: LocationRecipeBuilder.() -> Unit): LocationRecipeBuilder {
    return LocationRecipeBuilder(name).apply(initializer)
}