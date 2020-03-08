package traveling.location.location

import core.utility.NameSearchableList
import traveling.location.location.Location
import traveling.location.location.LocationNode

interface LocationParser {
    fun loadLocations(path: String): NameSearchableList<Location>
    fun loadLocationNodes(path: String): List<LocationNode>
}