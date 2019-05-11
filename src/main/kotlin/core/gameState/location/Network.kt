package core.gameState.location

import core.utility.NameSearchableList
import core.utility.Named

class Network(override val name: String, locationNodes: List<LocationNode> = listOf(), locations: List<Location> = listOf()) : Named{
    private val locationNodes = NameSearchableList(locationNodes)
    private val locations = NameSearchableList(locations)

    fun getLocationNode(name: String): LocationNode {
        return locationNodes.get(name)
    }

    fun getLocationNodes() : List<LocationNode> {
        return locationNodes.toList()
    }

    fun findLocation(name: String): LocationNode {
        return when {
            locationNodes.exists(name) -> locationNodes.get(name)
            name.startsWith("$") -> NOWHERE_NODE
            else -> {
                println("Could not find location: $name")
                NOWHERE_NODE
            }
        }
    }

    fun countLocationNodes(): Int {
        return locationNodes.size
    }

    fun getLocation(name: String): Location {
        return locations.getOrNull(name) ?: NOWHERE
    }

    fun getLocations(): List<Location> {
        return locations.toList()
    }

    fun locationExists(name: String): Boolean {
        return locations.exists(name)
    }

    // fun getLowestLocations() : List<LocationNode> {


}