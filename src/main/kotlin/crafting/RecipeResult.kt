package crafting

import core.gameState.Tags

data class RecipeResult(val name: String? = null, val id: String? = null, val tagsAdded: Tags = Tags(), val tagsRemoved: Tags = Tags()) {
    constructor(name: String) : this(name, null, Tags(), Tags())

}