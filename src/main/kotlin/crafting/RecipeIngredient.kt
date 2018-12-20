package crafting

import core.gameState.Item
import core.gameState.Tags

data class RecipeIngredient(val name: String? = null, val tags: Tags = Tags()) {
    constructor(name: String) : this(name, Tags())

    fun findMatchingIngredient(ingredients: List<Item>): Item? {
        val filtered = if (name != null) {
            ingredients.filter { it.name.toLowerCase() == name.toLowerCase() }
        } else {
            ingredients
        }

        return filtered.asSequence().filter { it.properties.tags.hasAll(tags) }.firstOrNull()
    }

}