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
}
