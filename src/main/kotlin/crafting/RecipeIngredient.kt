package crafting

import core.properties.Tags
import core.target.Target
import core.utility.wrapNonEmpty

data class RecipeIngredient(val name: String? = null, val tags: Tags = Tags()) {
    constructor(name: String) : this(name, Tags())

    init {
        if (name.isNullOrBlank() && tags.isEmpty()){
            throw IllegalArgumentException("Recipe Ingredient must have either a name or some tags.")
        }
    }

    fun findMatchingIngredient(ingredients: List<Target>): Target? {
        val filtered = if (name != null) {
            ingredients.filter { it.name.lowercase() == name.lowercase() }
        } else {
            ingredients
        }

        return filtered.asSequence().filter { it.properties.tags.hasAll(tags) }.firstOrNull()
    }

    fun read() : String {
        return (name ?: "Something") + tags.toString().wrapNonEmpty(" (", ")")
    }

}