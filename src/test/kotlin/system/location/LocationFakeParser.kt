package system.location

import core.PLAYER_START_LOCATION
import core.PLAYER_START_NETWORK
import core.utility.NameSearchableList
import traveling.location.Location
import traveling.location.LocationNode
import traveling.location.LocationParser

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