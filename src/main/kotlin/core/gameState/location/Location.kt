package core.gameState.location

import core.utility.Named

val NOWHERE = Location("Nowhere")

class Location(
        override val name: String,
        val description: String = "",
        val activators: List<LocationTarget> = listOf(),
        val creatures: List<LocationTarget> = listOf(),
        val items: List<LocationTarget> = listOf()
) : Named {

    override fun toString(): String {
        return name
    }

}