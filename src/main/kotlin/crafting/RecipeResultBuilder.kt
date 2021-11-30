package crafting

import core.thing.Thing
import core.thing.item.ItemManager

class RecipeResultBuilder {
    private var itemName: String? = null
    private var ingredientReference: String? = null
    private var description = ""
    private val tagsAdded = mutableListOf<String>()
    private val tagsRemoved = mutableListOf<String>()
    private var getItem: ((Map<String, Pair<RecipeIngredient, Thing>>) -> Thing)? = null

    /**
     * Get an item by this name
     */
    fun name(itemName: String) {
        this.itemName = itemName
    }

    /**
     * Refer to one of the ingredients
     */
    fun reference(ingredientReference: String) {
        this.ingredientReference = ingredientReference
    }

    fun description(description: String) {
        this.description = description
    }

    fun addTag(vararg tag: String) {
        this.tagsAdded.addAll(tag)
    }

    fun removeTag(vararg tag: String) {
        this.tagsRemoved.addAll(tag)
    }

    fun produces(getItem: ((Map<String, Pair<RecipeIngredient, Thing>>) -> Thing)) {
        this.getItem = getItem
    }

    fun build(): RecipeResult {
        //TODO - build description
        if (getItem != null) return RecipeResult(description, getItem!!)

        val baseItemGetter: (Map<String, Pair<RecipeIngredient, Thing>>) -> Thing = when {
            (ingredientReference != null) -> { usedIngredients -> (usedIngredients[ingredientReference!!.toString()])!!.second }
            (itemName != null) -> { _ -> ItemManager.getItem(itemName!!) }
            else -> throw IllegalStateException("Recipe must have an item name or item reference")
        }

        val transformation: (Map<String, Pair<RecipeIngredient, Thing>>) -> Thing = { usedIngredients ->
            baseItemGetter(usedIngredients).also { base ->
                base.properties.tags.addAll(tagsAdded)
                base.properties.tags.removeAll(tagsRemoved)
            }
        }

        return RecipeResult(description, transformation)
    }

}

fun result(initializer: RecipeResultBuilder.() -> Unit): RecipeResultBuilder {
    return RecipeResultBuilder().apply(initializer)
}

