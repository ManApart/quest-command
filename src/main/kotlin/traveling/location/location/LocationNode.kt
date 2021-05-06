package traveling.location.location

import com.fasterxml.jackson.annotation.JsonProperty
import core.utility.Named
import traveling.direction.Direction
import traveling.position.Vector
import traveling.location.Connection
import traveling.location.Network
import traveling.location.ProtoConnection
import traveling.location.RouteFinder
import kotlin.math.abs

val DEFAULT_NETWORK = Network("Wilderness")
val NOWHERE_NODE = LocationNode("Nowhere")

class LocationNode(
        override val name: String,
        val locationName: String = name,
        val parent: String = DEFAULT_NETWORK.name,
        var network: Network = DEFAULT_NETWORK,
        val isRoot: Boolean = false,
        @JsonProperty("locations") val protoConnections: List<ProtoConnection> = listOf(),
        private val connections: MutableList<Connection> = mutableListOf()
) : Named {
    constructor(base: LocationNode) : this(base.name, base.locationName, base.parent, base.network, base.isRoot, base.protoConnections)

    private var location: Location? = null
    var loadPath: String? = null

    override fun toString(): String {
        return name
    }

    fun addConnection(connection: Connection) {
        if (!hasConnection(connection)) {
            connections.add(connection)
        }
    }

    private fun hasConnection(connection: Connection): Boolean {
        return connections.any {
            it.source == connection.source && it.destination == connection.destination
        }
    }

    fun getNeighborConnections(): List<Connection> {
        return connections.toList()
    }

    fun getNeighbors(): List<LocationNode> {
        return connections.map { it.destination.location }
    }

    fun getNeighbors(direction: Direction): List<LocationNode> {
        return connections.asSequence()
                .filter { it.vector.direction == direction }
                .map { it.destination.location }
                .toList()
    }

    fun getNeighborsInGeneralDirection(direction: Direction): List<LocationNode> {
        return connections.asSequence()
                .filter { it.vector.isInGeneralDirection(direction) }
                .map { it.destination.location }
                .toList()
    }

    fun getLocationRecipe(): LocationRecipe {
        if (!network.locationRecipeExists(locationName)) {
            network.addLocationRecipe(LocationRecipe(locationName))
        }
        return network.getLocationRecipe(locationName)
    }

    fun hasLoadedLocation(): Boolean {
        return location != null
    }

    fun getLocation(): Location {
        return when {
            location != null -> location!!
            loadPath != null -> {
                location = load(loadPath!!, this)
                if (location == null) {
                    location = Location(this)
                }
                location!!
            }
            else -> {
                location = Location(this)
                location!!
            }
        }
    }

    fun flushLocation() {
        location = null
    }

    fun nameMatches(args: List<String>): Boolean {
        return name.lowercase().split(" ").contains(args[0])
    }

    fun getConnection(destination: LocationNode): Connection? {
        return connections.firstOrNull { it.destination.location == destination }
    }

    fun getSiblings(): String {
        val locations = getNeighborConnections()
        return if (locations.isNotEmpty()) {
            val siblings = locations.joinToString(", ") { getLocationWithDirection(it, false) }
            "is neighbored by $siblings"
        } else {
            "has no known neighbors"
        }
    }

    fun isMovingToRestricted(destination: LocationNode): Boolean {
        val link = getConnection(destination)
        return link == null || link.restricted
    }

    private fun getLocationWithDirection(neighbor: Connection, far: Boolean): String {
        val direction = neighbor.vector.direction
        return if (direction == Direction.NONE) {
            neighbor.destination.location.name
        } else {
            val farString = if (far) {
                "Far "
            } else ""
            "${neighbor.destination.location.name} ($farString$direction)"
        }
    }

    fun getDistanceToLowestNodeInNetwork(): Int {
        val lowestNodes = network.getFurthestLocations(Direction.BELOW)
        return if (lowestNodes.isEmpty()) {
            0
        } else {
            lowestNodes.map { abs(getVectorDistanceTo(it).z) }.maxOrNull() ?: 0
        }
    }

    fun getVectorDistanceTo(other: LocationNode): Vector {
        val route = RouteFinder(this, other)
        return if (route.hasRoute()) {
            route.getRoute().getVectorDistance()
        } else {
            Vector()
        }
    }

    fun getDistanceTo(other: LocationNode?): Int {
        if (other == null) {
            return 0
        }

        val route = RouteFinder(this, other)
        return if (route.hasRoute()) {
            route.getRoute().getDistance()
        } else {
            0
        }
    }

    fun isAnOuterNode(direction: Direction): Boolean {
        val furthestNodes = network.getFurthestLocations(direction)
        return direction != Direction.NONE && (furthestNodes.isEmpty() || furthestNodes.contains(this))
    }

}