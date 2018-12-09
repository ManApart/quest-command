package core.gameState.location

import core.utility.Named

class Location(override val name: String, val description: String = "", val activators: List<String> = listOf(), val creatures: List<String> = listOf(), val items: List<String> = listOf()) : Named {

    override fun toString(): String {
        return name
    }

}