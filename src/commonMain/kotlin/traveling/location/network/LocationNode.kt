package traveling.location.network

import core.thing.Thing
import core.utility.Named
import kotlinx.serialization.SerialName
import traveling.direction.Direction
import traveling.location.Connection
import traveling.location.ConnectionRecipe
import traveling.location.Network
import traveling.location.RouteFinder
import traveling.location.location.Location
import traveling.location.location.LocationRecipe
import traveling.location.location.load
import traveling.location.location.location
import traveling.position.Vector
import kotlin.math.abs

val DEFAULT_NETWORK = Network("Wilderness")
val NOWHERE_NODE = LocationNode("Nowhere")

data class LocationNode(
    override val name: String,
    val locationName: String = name,
    val parent: String = DEFAULT_NETWORK.name,
    var network: Network = DEFAULT_NETWORK,
    val isRoot: Boolean = false,
    val recipe: LocationRecipe = LocationRecipe(name),
    @SerialName("locations") val connectionRecipes: List<ConnectionRecipe> = listOf(),
    private val connections: MutableList<Connection> = mutableListOf(),
    var loadPath: String? = null,
) : Named {
    constructor(base: LocationNode) : this(base.name, base.locationName, base.parent, base.network, base.isRoot, base.recipe, base.connectionRecipes, loadPath = base.loadPath)

    private var location: Location? = null


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
        return connections
            .filter { it.vector.direction == direction }
            .map { it.destination.location }
    }

    fun getNeighborsInGeneralDirection(direction: Direction): List<LocationNode> {
        return connections
            .filter { it.vector.isInGeneralDirection(direction) }
            .map { it.destination.location }
    }

    fun hasLoadedLocation(): Boolean {
        return location != null
    }

    suspend fun getLocation(): Location {
        return when {
            location != null -> location!!
            loadPath != null -> {
                location = load(loadPath!!, this)
                location!!
            }
            else -> {
                location = location(this)
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

    fun getSiblings(includeHidden: Boolean = true): String {
        val locations = getNeighborConnections().filter { includeHidden || !it.hidden }
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

    fun discoverSelfAndNeighbors(creature: Thing) {
        creature.mind.discover(this)
        connections.filter { !it.hidden }.map { it.destination.location }.forEach { neighbor ->
            creature.mind.discover(neighbor)
        }
    }

    fun getPositionRelativeTo(neighbor: LocationNode): Vector? {
        return connections.firstOrNull { it.destination.location == neighbor }?.source?.vector?.invert()
    }

}