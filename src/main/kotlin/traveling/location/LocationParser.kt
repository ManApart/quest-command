package traveling.location

import core.utility.NameSearchableList

interface LocationParser {
    fun loadLocations(): NameSearchableList<Location>
    fun loadLocationNodes(): List<LocationNode>
}