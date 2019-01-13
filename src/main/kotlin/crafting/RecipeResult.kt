package crafting

import core.gameState.Item
import core.gameState.Tags
import core.utility.wrapNonEmpty
import system.item.ItemManager

data class RecipeResult(val name: String? = null, val id: Int? = null, val tagsAdded: Tags = Tags(), val tagsRemoved: Tags = Tags()) {
    constructor(name: String) : this(name, null, Tags(), Tags())

    init {
        if (name.isNullOrBlank() && id == null) {
            throw IllegalArgumentException("Recipe Result must have either a name or an ID.")
        }
    }

    fun getResult(usedIngredients: List<Item>): Item {
        val item = if (!name.isNullOrBlank()) {
            ItemManager.getItem(name!!)
        } else {
            if (usedIngredients.size <= id!!) {
                throw IllegalArgumentException("Recipe Result had id $id but only ${usedIngredients.size} used ingredients")
            } else {
                usedIngredients[id]
            }
        }
        tagsRemoved.getAll().forEach {
            item.properties.tags.remove(it)
        }

        tagsAdded.getAll().forEach {
            item.properties.tags.add(it)
        }

        return item
    }

    fun read() : String {
        return (name ?: "Something") + tagsAdded.toString().wrapNonEmpty(" (", ")")
    }

}