package core.gameState

import com.fasterxml.jackson.annotation.JsonCreator

class Item(override val name: String, override val description: String, val weight: Int, override val tags: Tags, override val properties: Properties) : Target {
    override fun toString(): String {
        return name
    }

    @JsonCreator
    constructor(name: String, description: String = "", weight: Int = 0, tags: List<String> = listOf(), properties: Map<String, String> = HashMap()) : this(name, description, weight, Tags(tags), Properties(properties))

    fun copy() : Item {
        return Item(name, description, weight, tags, properties)
    }
}