package crafting

import core.properties.Properties
import core.properties.PropsBuilder
import core.properties.Tags
import core.thing.Thing
import core.utility.MapBuilder

class RecipeIngredientBuilder {
    private var name: String? = null
    private val tags = mutableListOf<String>()
    private val skills: MapBuilder = MapBuilder()
    private var toolProps = PropsBuilder()

    //Crafter, Ingredient, Tool?
    private var criteria = mutableListOf<(Thing, Thing, Thing?) -> Boolean>()

    fun name(name: String) {
        this.name = name.lowercase()
    }

    fun tag(vararg tags: String) {
        this.tags.addAll(tags)
    }

    fun skill(vararg values: Pair<String, Any>) = this.skills.entry(values.toList())
    fun skill(key: String, value: Int) = skills.entry(key to value.toString())

    fun toolProps(initializer: PropsBuilder.() -> Unit) {
        toolProps.apply(initializer)
    }

    fun matches(criteria: (Thing, Thing, Thing?) -> Boolean) {
        this.criteria.add(criteria)
    }

    private fun matchesName(ingredient: Thing): Boolean {
        return ingredient.name.lowercase() == name && matchesTags(ingredient)
    }

    private fun matchesTags(ingredient: Thing): Boolean {
        return ingredient.properties.tags.hasAll(Tags(tags))
    }

    private fun matchesSkills(crafter: Thing, skillMap: Map<String, Int>): Boolean {
        return skillMap.all { (skillName, skillValue) ->
            val current = crafter.soul.getStatOrNull(skillName)
            return (current?.current ?: 0) > skillValue
        }
    }

    private fun matchesTool(tool: Thing?, props: Properties): Boolean {
        return (tool?.properties ?: Properties()).hasAll(props)
    }

    fun build(): RecipeIngredient2 {
        if (name != null) criteria.add { _, ingredient, _ -> matchesName(ingredient) }
        if (tags.isNotEmpty()) criteria.add { _, ingredient, _ -> matchesTags(ingredient) }

        val skillMap = skills.build().mapValues { it.value.toInt() }
        if (skillMap.isNotEmpty()) criteria.add { crafter, _, _ -> matchesSkills(crafter, skillMap) }

        val properties = toolProps.build()
        if (properties.isNotEmpty()) criteria.add { _, _, tool -> matchesTool(tool, properties) }

        if (criteria.isEmpty()) throw IllegalArgumentException("Recipe must include at least one criteria.")

        return RecipeIngredient2 { crafter, ingredient, tool ->
            criteria.all { it(crafter, ingredient, tool) }
        }
    }
}


fun ingredient(initializer: RecipeIngredientBuilder.() -> Unit): RecipeIngredientBuilder {
    return RecipeIngredientBuilder().apply(initializer)
}