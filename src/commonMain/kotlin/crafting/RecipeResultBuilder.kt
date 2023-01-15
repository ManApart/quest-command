package crafting

import core.thing.Thing
import core.thing.item.ItemManager
import core.utility.capitalize2
import core.utility.joinToStringAnd
import core.utility.wrapNonEmpty

class RecipeResultBuilder {
    private var itemName: String? = null
    private var ingredientReference: String? = null
    private var description = ""
    private val tagsAdded = mutableListOf<String>()
    private val tagsRemoved = mutableListOf<String>()

    //Crafter, Tool? Ingredients
    private var getItem: ((Thing, Thing?, Map<String, Pair<RecipeIngredient, Thing>>) -> Thing)? = null

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

    fun produces(getItem: ((Thing, Thing?, Map<String, Pair<RecipeIngredient, Thing>>) -> Thing)) {
        this.getItem = getItem
    }

    suspend fun build(): RecipeResult {
        if (description.isBlank()) buildDescription()

        if (getItem != null) return RecipeResult(description, getItem!!)

        val baseItemGetter: (Map<String, Pair<RecipeIngredient, Thing>>) -> Thing = when {
            (ingredientReference != null) -> { usedIngredients -> (usedIngredients[ingredientReference!!.toString()])!!.second }
            (itemName != null) -> { _ -> ItemManager.getItem(itemName!!) }
            else -> throw IllegalStateException("Recipe must have an item name or item reference")
        }

        val transformation: (Thing, Thing?, Map<String, Pair<RecipeIngredient, Thing>>) -> Thing = { _, _, usedIngredients ->
            baseItemGetter(usedIngredients).also { base ->
                base.properties.tags.addAll(tagsAdded)
                base.properties.tags.removeAll(tagsRemoved)
            }
        }

        return RecipeResult(description, transformation)
    }

    private fun buildDescription() {
        val nameString = itemName?.capitalize2() ?: ingredientReference?.capitalize2() ?: "Something"
        val tagAddedString = if (tagsAdded.isNotEmpty()) tagsAdded.joinToStringAnd().wrapNonEmpty("(Adding ", ")") else null
        val tagRemovedString = if (tagsRemoved.isNotEmpty()) tagsRemoved.joinToStringAnd().wrapNonEmpty("(Removing ", ")") else null
        description = listOfNotNull(nameString, tagAddedString, tagRemovedString).joinToString(" ")
    }

}

fun result(initializer: RecipeResultBuilder.() -> Unit): RecipeResultBuilder {
    return RecipeResultBuilder().apply(initializer)
}

