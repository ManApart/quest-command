package crafting

import core.target.TargetBuilder
import core.target.TargetsBuilder
import core.utility.NameSearchableList
import core.utility.toNameSearchableList

class RecipesBuilder {
    internal val children = mutableListOf<RecipeBuilder>()

    fun target(item: RecipeBuilder) {
        children.add(item)
    }

    fun target(name: String, initializer: RecipeBuilder.() -> Unit) {
        children.add(RecipeBuilder(name).apply(initializer))
    }
}

fun recipes(initializer: RecipesBuilder.() -> Unit): List<RecipeBuilder> {
    return RecipesBuilder().apply(initializer).children
}

fun List<RecipeBuilder>.build(): NameSearchableList<Recipe> {
    val builders = associateBy { it.name }
    return builders.values.map {
        try {
            it.build()
        } catch (e: Exception) {
            println("Failed to build ${it.name}: ${e.message ?: e.cause ?: e.toString()}")
            throw  e
        }
    }.toNameSearchableList()
}