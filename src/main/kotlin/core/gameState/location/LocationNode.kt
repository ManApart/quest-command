package core.gameState.location

import com.fasterxml.jackson.annotation.JsonProperty
import core.gameState.Direction
import core.utility.Named
import system.location.LocationManager

val NOWHERE_NODE = LocationNode("Nowhere")

class LocationNode(
        override val name: String,
        val locationName: String = name,
        val parent: String? = null,
        @JsonProperty("locations") val protoLocationLinks: List<ProtoLocationLink> = listOf(),
        private val locationLinks: MutableList<LocationLink> = mutableListOf()
) : Named {

    override fun toString(): String {
        return name
    }

    fun getDescription(): String {
        return name
    }

    fun addLink(link: LocationLink) {
        if (!hasLink(link)) {
            locationLinks.add(link)
        }
    }

    private fun hasLink(link: LocationLink): Boolean {
        return locationLinks.any {
            it.source == link.source && it.destination == link.destination
        }
    }

    fun getNeighborLinks(): List<LocationLink> {
        return locationLinks.toList()
    }

    fun getNeighbors(): List<LocationNode> {
        return locationLinks.map { it.destination }
    }

    fun getNeighbors(direction: Direction): List<LocationNode> {
        return locationLinks.asSequence()
                .filter { it.position.getDirection() == direction }
                .map { it.destination }
                .toList()
    }

    fun getLocation(): Location {
        return LocationManager.getLocation(locationName)
    }

    fun nameMatches(args: List<String>): Boolean {
        return name.toLowerCase().split(" ").contains(args[0])
    }

    fun getLink(destination: LocationNode): LocationLink? {
        return locationLinks.firstOrNull { it.destination == destination }
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

    fun isMovingToRestricted(destination: LocationNode): Boolean {
        val link = getLink(destination)
        return link == null || link.restricted
    }

    private fun getLocationWithDirection(neighbor: LocationLink, far: Boolean): String {
        val direction = neighbor.position.getDirection()
        return if (direction == Direction.NONE) {
            neighbor.destination.name
        } else {
            val farString = if (far) {
                "Far "
            } else ""
            "${neighbor.destination.name} ($farString$direction)"
        }
    }

//    fun findRouteTo(destination: LocationNode): Route {
//
//    }
//


}