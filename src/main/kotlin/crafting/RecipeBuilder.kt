package crafting

import core.properties.PropsBuilder
import core.properties.Tags
import core.utility.MapBuilder

class RecipeBuilder(internal val name: String) {
    private var verb = "craft"
    private val skills: MapBuilder = MapBuilder()
    private var toolProps = PropsBuilder()
    private val ingredients = mutableListOf<RecipeIngredient>()
    private val results = mutableListOf<RecipeResultBuilder>()

    fun build(): Recipe {
        val skillMap = skills.build().mapValues { it.value.toInt() }
        val toolProperties = toolProps.build()
        val resultItems = results.map { it.build() }
        return Recipe(name, ingredients, skillMap, toolProperties, resultItems, verb)
    }

    fun verb(verb: String) {
        this.verb = verb
    }

    fun skill(vararg values: Pair<String, Any>) = this.skills.entry(values.toList())
    fun skill(key: String, value: String) = skills.entry(key to value)
    fun skill(key: String, value: Int) = skills.entry(key to value.toString())

    fun ingredientNamed(name: String) {
        ingredients.add(RecipeIngredient(name))
    }

    fun ingredient(vararg tags: String) {
        ingredients.add(RecipeIngredient(tags = Tags(tags.toMutableList())))
    }

    fun ingredient(name: String, tags: List<String>) {
        ingredients.add(RecipeIngredient(name, Tags(tags.toMutableList())))
    }

    fun toolProps(initializer: PropsBuilder.() -> Unit) {
        toolProps.apply(initializer)
    }

    fun result(name: String) {
        results.add(RecipeResultBuilder().apply { name(name) })
    }

    fun result(initializer: RecipeResultBuilder.() -> Unit) {
        results.add(RecipeResultBuilder().apply(initializer))
    }

}

fun recipe(name: String, initializer: RecipeBuilder.() -> Unit): RecipeBuilder {
    return RecipeBuilder(name).apply(initializer)
}