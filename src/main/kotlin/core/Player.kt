package core

import core.target.Target
import core.utility.NameSearchableList
import crafting.Recipe
import traveling.location.Route
import traveling.location.network.LocationNode

data class Player(
    val id: Int,
    val target: Target
) {
    val knownLocations: NameSearchableList<LocationNode> = NameSearchableList()
    val knownRecipes: NameSearchableList<Recipe> = NameSearchableList()
    var compassRoute: Route? = null

    fun isPlayer(): Boolean {
        return target.isPlayer()
    }

    val soul get() = target.soul
    val body get() = target.body
    val properties get() = target.properties
    val ai get() = target.ai
    val inventory get() = target.inventory
}
