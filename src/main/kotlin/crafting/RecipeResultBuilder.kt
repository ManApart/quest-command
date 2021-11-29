package crafting

import core.thing.Thing
import core.thing.item.ItemManager

class RecipeResultBuilder {
    private var name: String? = null
    private var id: Int? = null
    private val tagsAdded = mutableListOf<String>()
    private val tagsRemoved = mutableListOf<String>()

    fun name(name: String){
        this.name = name
    }

    fun id(id: Int){
        this.id = id
    }

    fun addTag(vararg tags: String){
        tagsAdded.addAll(tags.toList())
    }

    fun removeTag(vararg tags: String){
        tagsRemoved.addAll(tags.toList())
    }

    fun build(): RecipeResult {
        return RecipeResult(id?.toString() ?: name!!, ::legacyGet)
    }

    //Back compat until builder is switched over
    private fun legacyGet(usedIngredients: Map<String, Pair<RecipeIngredient, Thing>>): Thing {
        val item = if (!name.isNullOrBlank()) {
            ItemManager.getItem(name!!)
        } else {
            if (usedIngredients.size <= id!!) {
                throw IllegalArgumentException("Recipe Result had id $id but only ${usedIngredients.size} used ingredients")
            } else {
                (usedIngredients[id!!.toString()])!!.second
            }
        }
        tagsRemoved.forEach {
            item.properties.tags.remove(it)
        }

        tagsAdded.forEach {
            item.properties.tags.add(it)
        }

        return item
    }

}

fun result(initializer: RecipeResultBuilder.() -> Unit): RecipeResultBuilder {
    return RecipeResultBuilder().apply(initializer)
}

