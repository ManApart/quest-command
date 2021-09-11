package crafting

import core.properties.Tags

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
        return RecipeResult(name, id, Tags(tagsAdded), Tags(tagsRemoved))
    }
}

fun result(initializer: RecipeResultBuilder.() -> Unit): RecipeResultBuilder {
    return RecipeResultBuilder().apply(initializer)
}