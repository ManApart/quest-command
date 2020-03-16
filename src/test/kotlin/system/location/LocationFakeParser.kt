package system.location

import core.PLAYER_START_LOCATION
import core.PLAYER_START_NETWORK
import core.utility.NameSearchableList
import traveling.location.location.LocationRecipe
import traveling.location.location.LocationNode
import traveling.location.location.LocationParser

class LocationFakeParser(
        private val locationRecipes: NameSearchableList<LocationRecipe> = NameSearchableList(),
        private val locationNodes: NameSearchableList<LocationNode> = NameSearchableList(listOf(LocationNode(PLAYER_START_LOCATION, parent = PLAYER_START_NETWORK)))
) : LocationParser {

    override fun loadLocations(path: String): NameSearchableList<LocationRecipe> {
        return locationRecipes
    }

    override fun loadLocationNodes(path: String): List<LocationNode> {
        return locationNodes
    }
}