package traveling.location.location

class LocationRecipesBuilder {
    internal val children = mutableListOf<LocationRecipeBuilder>()

    fun location(item: LocationRecipeBuilder) {
        children.add(item)
    }

    fun location(name: String, initializer: LocationRecipeBuilder.() -> Unit = {}) {
        children.add(LocationRecipeBuilder(name).apply(initializer))
    }
}

fun locations(initializer: LocationRecipesBuilder.() -> Unit): List<LocationRecipeBuilder> {
    return LocationRecipesBuilder().apply(initializer).children
}

fun List<LocationRecipeBuilder>.build(): List<LocationRecipe> {
    val builders = associateBy { it.name }
    return builders.values.map {
        try {
            it.buildWithBase(builders)
        } catch (e: Exception) {
            println("Failed to build ${it.name}: ${e.message ?: e.cause ?: e.toString()}")
            throw  e
        }
    }
}