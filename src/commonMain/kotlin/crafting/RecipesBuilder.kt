package crafting

import core.utility.NameSearchableList
import core.utility.applySuspending
import core.utility.toNameSearchableList

class RecipesBuilder {
    internal val children = mutableListOf<RecipeBuilder>()

    fun recipe(item: RecipeBuilder) {
        children.add(item)
    }

    suspend fun recipe(name: String, initializer: suspend RecipeBuilder.() -> Unit) {
        children.add(RecipeBuilder(name).applySuspending(initializer))
    }
}

suspend fun recipes(initializer: suspend RecipesBuilder.() -> Unit): List<RecipeBuilder> {
    return RecipesBuilder().applySuspending(initializer).children
}

suspend fun List<RecipeBuilder>.build(): NameSearchableList<Recipe> {
    val builders = associateBy { it.name }
    return builders.values.map {
        try {
            it.build()
        } catch (e: Exception) {
            println("Failed to build ${it.name}: ${e.message ?: e.cause ?: e.toString()}")
            throw e
        }
    }.toNameSearchableList()
}