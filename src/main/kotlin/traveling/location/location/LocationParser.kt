package traveling.location.location

import core.utility.NameSearchableList

interface LocationParser {
    fun loadLocations(path: String): NameSearchableList<LocationRecipe>
    fun loadLocationNodes(path: String): List<LocationNode>
}