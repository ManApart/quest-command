package core.gameState

import com.fasterxml.jackson.annotation.JsonCreator

class Item(override val name: String, override val description: String ="", val weight: Int = 0, override val properties: Properties = Properties()) : Target {
    override fun toString(): String {
        return name
    }

//    @JsonCreator
//    constructor(name: String, description: String = "", weight: Int = 0, tags: List<String> = listOf(), values: Map<String, String> = HashMap()) : this(name, description, weight, Tags(tags), PropertyValues(values))

    fun copy() : Item {
        return Item(name, description, weight, properties)
    }
}