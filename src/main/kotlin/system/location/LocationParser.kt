package system.location

import core.gameState.location.Location
import core.gameState.location.LocationNode
import core.utility.NameSearchableList

interface LocationParser {
    fun loadLocations(): NameSearchableList<Location>
    fun loadLocationNodes(): List<LocationNode>
}