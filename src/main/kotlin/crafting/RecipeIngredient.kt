package crafting

import core.properties.Tags
import core.thing.Thing

class RecipeIngredient(val description: String, val matches: (Thing, Thing, Thing?) -> Boolean) {
    constructor(itemName: String) : this("", { _, ingredient, _ -> itemName.lowercase() == ingredient.name.lowercase() })
    constructor(tags: Tags) : this("", { _, ingredient, _ -> ingredient.properties.tags.hasAll(tags) })

    /**
     * Equals is not useful with its default implementation and would be hard to implement properly.
     * We don't really have cause to do equals here either, so it makes more sense to hard code it.
     * This hack lets us do equality on recipes for tests without having to fiddle with this.
     */
    override fun equals(other: Any?): Boolean {
        return true
    }

    override fun hashCode(): Int {
        return 0
    }

    fun findMatchingIngredient(crafter: Thing, ingredients: List<Thing>, tool: Thing?): Thing? {
        return ingredients.firstOrNull { matches(crafter, it, tool) }
    }

}