package core.gameState.location

import core.gameState.Direction
import core.utility.Named
import system.location.LocationManager

class LocationNode(override val name: String, val locationName: String = name, val parent: String? = null, private val locations: MutableList<LocationLink> = mutableListOf()) : Named {

    override fun toString(): String {
        return name
    }

    fun getDescription(): String {
        return name
    }

    fun addLink(link: LocationLink) {
        if (!hasLink(link)) {
            locations.add(link)
        }
    }

    private fun hasLink(link: LocationLink): Boolean {
        return locations.any { it.name == link.name }
    }

    fun getNeighborLinks(): List<LocationLink> {
        return locations.toList()
    }

    fun getNeighbors(): List<LocationNode> {
        return locations.map {
            LocationManager.getLocationNode(it.name)
        }
    }

    fun getNeighbors(direction: Direction): List<LocationNode> {
        val links = locations.filter {
            it.position.getDirection() == direction
        }
        return links.map {
            LocationManager.getLocationNode(it.name)
        }
    }

    fun getLocation(): Location {
        return LocationManager.getLocation(locationName)
    }

    fun nameMatches(args: List<String>): Boolean {
        return name.toLowerCase().split(" ").contains(args[0])
    }

    fun getLink(destination: LocationNode): LocationLink? {
        return locations.first { it.name == destination.name }
    }

    fun getSiblings(): String {
        val locations = getNeighborLinks()
        return if (locations.isNotEmpty()) {
            val siblings = locations.joinToString(", ") { getLocationWithDirection(it, false) }
            "is neighbored by $siblings"
        } else {
            "has no known neighbors"
        }
    }

    private fun getLocationWithDirection(neighbor: LocationLink, far: Boolean): String {
        val direction = neighbor.position.getDirection()
        return if (direction == Direction.NONE) {
            neighbor.name
        } else {
            val farString = if (far) {
                "Far "
            } else ""
            "${neighbor.name} ($farString$direction)"
        }
    }

//    fun getNearestNeighbor(direction: Direction): LocationNode {
//
//    }

//    fun getPathTo(destination: LocationNode): List<LocationNode> {
//
//    }
//
//    fun getDistanceTo(destination: LocationNode): List<LocationNode> {
//
//    }
//
//    private fun getDistanceOfPath(path: List<LocationNode>) : Int {
//
//    }


}