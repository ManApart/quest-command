package core.gameState.location

import com.fasterxml.jackson.annotation.JsonProperty
import core.gameState.Direction
import core.utility.Named
import system.location.LocationManager

val NOWHERE_NODE = LocationNode("Nowhere")
const val DEFAULT_NETWORK = "Wilderness"

class LocationNode(
        override val name: String,
        val locationName: String = name,
        val parent: String = DEFAULT_NETWORK,
        @JsonProperty("locations") val protoConnections: List<ProtoConnection> = listOf(),
        private val locationLinks: MutableList<Connection> = mutableListOf()
) : Named {

    override fun toString(): String {
        return name
    }

    fun getDescription(): String {
        return name
    }

    fun addLink(link: Connection) {
        if (!hasLink(link)) {
            locationLinks.add(link)
        }
    }

    private fun hasLink(link: Connection): Boolean {
        return locationLinks.any {
            it.source == link.source && it.destination == link.destination
        }
    }

    fun getNeighborLinks(): List<Connection> {
        return locationLinks.toList()
    }

    fun getNeighbors(): List<LocationNode> {
        return locationLinks.map { it.destination.location }
    }

    fun getNeighbors(direction: Direction): List<LocationNode> {
        return locationLinks.asSequence()
                .filter { it.vector.getDirection() == direction }
                .map { it.destination.location }
                .toList()
    }

    fun getNetwork(): Network{
        return LocationManager.getNetwork(parent)
    }

    fun getLocation(): Location {
        return getNetwork().getLocation(locationName)
    }

    fun nameMatches(args: List<String>): Boolean {
        return name.toLowerCase().split(" ").contains(args[0])
    }

    fun getLink(destination: LocationNode): Connection? {
        return locationLinks.firstOrNull { it.destination.location == destination }
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

    private fun getLocationWithDirection(neighbor: Connection, far: Boolean): String {
        val direction = neighbor.vector.getDirection()
        return if (direction == Direction.NONE) {
            neighbor.destination.location.name
        } else {
            val farString = if (far) {
                "Far "
            } else ""
            "${neighbor.destination.location.name} ($farString$direction)"
        }
    }

    fun getDistanceToLowestNodeInNetwork() : Int {
        val lowestNode = getNetwork().getFurthestLocations(Direction.BELOW).first()
        val route = RouteFinder(this, lowestNode)
        return if (route.hasRoute()) {
            route.getRoute().getDistance()
        } else {
            0
        }
    }


}