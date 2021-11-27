package crafting

import core.properties.Properties
import core.properties.PropsBuilder
import core.properties.Tags
import core.thing.Thing
import core.utility.MapBuilder
import core.utility.capitalize2
import core.utility.joinToStringAnd
import core.utility.wrapNonEmpty

class RecipeIngredientBuilder {
    private var description = ""
    private var name: String? = null
    private val tags = mutableListOf<String>()
    private val skills: MapBuilder = MapBuilder()
    private var toolProps = PropsBuilder()

    //Crafter, Ingredient, Tool?
    private var criteria = mutableListOf<(Thing, Thing, Thing?) -> Boolean>()

    fun name(name: String) {
        this.name = name.lowercase()
    }

    fun description(description: String) {
        this.description = description
    }

    fun tag(tags: List<String>) {
        this.tags.addAll(tags)
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
        return ingredient.name.lowercase() == name
    }

    private fun matchesTags(ingredient: Thing): Boolean {
        return ingredient.properties.tags.hasAll(Tags(tags))
    }

    private fun matchesSkills(crafter: Thing, skillMap: Map<String, Int>): Boolean {
        return skillMap.all { (skillName, skillValue) ->
            val current = crafter.soul.getStatOrNull(skillName)
            return (current?.current ?: 0) >= skillValue
        }
    }

    private fun matchesTool(tool: Thing?, props: Properties): Boolean {
        return (tool?.properties ?: Properties()).hasAll(props)
    }

    fun build(): RecipeIngredient {
        if (!name.isNullOrBlank()) criteria.add { _, ingredient, _ -> matchesName(ingredient) }
        if (tags.isNotEmpty()) criteria.add { _, ingredient, _ -> matchesTags(ingredient) }

        val skillMap = skills.build().mapValues { it.value.toInt() }
        if (skillMap.isNotEmpty()) criteria.add { crafter, _, _ -> matchesSkills(crafter, skillMap) }

        val properties = toolProps.build()
        if (properties.isNotEmpty()) criteria.add { _, _, tool -> matchesTool(tool, properties) }

        if (criteria.isEmpty()) throw IllegalArgumentException("Recipe must include at least one criteria.")

        if (description.isBlank()) buildDescription(skillMap, properties)

        return RecipeIngredient(description) { crafter, ingredient, tool ->
            criteria.all { it(crafter, ingredient, tool) }
        }
    }

    private fun buildDescription(skillMap: Map<String, Int>, toolProps: Properties) {
        val nameString = name?.capitalize2() ?: "Something"
        val tagString = if (tags.isNotEmpty()) tags.joinToStringAnd().wrapNonEmpty("(", ")") else null
        val toolString = if (toolProps.isNotEmpty()) toolProps.toString() else null
        val skillString = if (skillMap.isNotEmpty()) skillMap.toString().wrapNonEmpty("(", ")") else null

        description = listOfNotNull(nameString, tagString, toolString, skillString).joinToString(" ") { it }
    }
}


fun ingredient(initializer: RecipeIngredientBuilder.() -> Unit): RecipeIngredient {
    return RecipeIngredientBuilder().apply(initializer).build()
}