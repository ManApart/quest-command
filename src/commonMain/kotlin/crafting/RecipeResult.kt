package crafting

import core.thing.Thing
import core.thing.item.ItemManager


class RecipeResult(val description: String, val getResult: ((Thing, Thing?, Map<String, Pair<RecipeIngredient, Thing>>) -> Thing)) {
    constructor(name: String) : this(name, { _, _, _ -> ItemManager.getItem(name) })
    constructor(reference: String, tagsAdded: List<String>, tagsRemoved: List<String>) : this("Something", { _, _, ingredients ->
        val result = ingredients[reference]!!.second
        result.properties.tags.addAll(tagsAdded)
        result.properties.tags.removeAll(tagsRemoved)
        result
    })

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

}

