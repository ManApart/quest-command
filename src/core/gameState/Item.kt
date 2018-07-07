package core.gameState

import com.fasterxml.jackson.annotation.JsonCreator

class Item(override val name: String, val description: String, override val tags: Tags, val properties: Properties) : Target {
    override fun toString(): String {
        return name
    }

    @JsonCreator
    constructor(name: String, description: String = "", tags: List<String> = listOf(), properties: Map<String, String> = HashMap()) : this(name, description, Tags(tags), Properties(properties))

    fun copy() : Item {
        return Item(name, description, tags, properties)
    }
}