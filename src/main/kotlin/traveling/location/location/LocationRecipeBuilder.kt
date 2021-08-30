package traveling.location.location

import core.properties.PropsBuilder

class LocationRecipeBuilder(val name: String) {
    private var propsBuilder = PropsBuilder()

    fun props(initializer: PropsBuilder.() -> Unit) {
        propsBuilder.apply(initializer)
    }

    internal fun build(): LocationRecipe {
        val props = propsBuilder.build()

        return LocationRecipe(
            name,
            properties = props,
        )
    }

}

fun locationRecipe(name: String, initializer: LocationRecipeBuilder.() -> Unit): LocationRecipe {
    return LocationRecipeBuilder(name).apply(initializer).build()
}