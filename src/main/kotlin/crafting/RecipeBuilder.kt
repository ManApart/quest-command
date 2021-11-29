package crafting

import core.properties.PropsBuilder
import core.utility.MapBuilder

class RecipeBuilder(internal val name: String) {
    private var verb = "craft"
    private val skills: MapBuilder = MapBuilder()
    private var toolProps = PropsBuilder()
    private val ingredients = mutableMapOf<String, RecipeIngredient>()
    private val results = mutableListOf<RecipeResultBuilder>()
    private var id = 0

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
    fun skill(key: String, value: Int) = skills.entry(key to value.toString())

    fun ingredient(vararg tags: String) {
        val reference = (id++).toString()
        ingredient(reference) {
            tag(tags.toList())
        }
    }

    fun ingredientNamed(name: String, initializer: RecipeIngredientBuilder.() -> Unit = {}) {
        ingredients[name] = RecipeIngredientBuilder().apply { name(name) }.apply(initializer).build()
    }

    fun ingredient(reference: String, initializer: RecipeIngredientBuilder.() -> Unit) {
        ingredients[reference] = RecipeIngredientBuilder().apply(initializer).build()
    }

    fun ingredient(initializer: RecipeIngredientBuilder.() -> Unit) {
        val reference = (id++).toString()
        ingredients[reference] = RecipeIngredientBuilder().apply(initializer).build()
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