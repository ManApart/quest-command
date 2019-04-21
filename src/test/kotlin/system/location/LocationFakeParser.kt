package system.location

import core.gameState.PLAYER_START_LOCATION
import core.gameState.PLAYER_START_NETWORK
import core.gameState.location.Location
import core.gameState.location.LocationNode
import core.utility.NameSearchableList

class LocationFakeParser(
        private val locations: NameSearchableList<Location> = NameSearchableList(),
        private val locationNodes: NameSearchableList<LocationNode> = NameSearchableList(listOf(LocationNode(PLAYER_START_LOCATION, parent = PLAYER_START_NETWORK)))
) : LocationParser {

    override fun loadLocations(): NameSearchableList<Location> {
        return locations
    }

    override fun loadLocationNodes(): List<LocationNode> {
        return locationNodes
    }
}