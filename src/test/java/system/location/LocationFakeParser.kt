package system.location

import core.gameState.location.Location
import core.gameState.location.LocationNode
import core.utility.NameSearchableList

class LocationFakeParser(private val locations: NameSearchableList<Location> = NameSearchableList(), private val locationNodes: NameSearchableList<LocationNode> = NameSearchableList()) : LocationParser {

    override fun loadLocations(): NameSearchableList<Location> {
        return locations
    }

    override fun loadLocationNodes(): List<LocationNode> {
        return locationNodes
    }
}