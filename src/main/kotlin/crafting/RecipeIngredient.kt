package crafting

import core.gameState.Item
import core.gameState.Tags
import core.utility.wrapNonEmpty
import java.lang.IllegalArgumentException

data class RecipeIngredient(val name: String? = null, val tags: Tags = Tags()) {
    constructor(name: String) : this(name, Tags())

    init {
        if (name.isNullOrBlank() && tags.isEmpty()){
            throw IllegalArgumentException("Recipe Ingredient must have either a name or some tags.")
        }
    }

    fun findMatchingIngredient(ingredients: List<Item>): Item? {
        val filtered = if (name != null) {
            ingredients.filter { it.name.toLowerCase() == name.toLowerCase() }
        } else {
            ingredients
        }

        return filtered.asSequence().filter { it.properties.tags.hasAll(tags) }.firstOrNull()
    }

    fun read() : String {
        return (name ?: "Something") + tags.toString().wrapNonEmpty(" (", ")")
    }

}