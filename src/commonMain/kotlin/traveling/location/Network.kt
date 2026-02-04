package traveling.location

import core.utility.NameSearchableList
import core.utility.Named
import system.persistance.cleanPathPart
import traveling.direction.Direction
import traveling.location.location.LocationPoint
import traveling.location.location.LocationRecipe
import traveling.location.location.NOWHERE
import traveling.location.network.LocationNode
import traveling.location.network.NOWHERE_NODE
import traveling.position.Vector

class Network(override val name: String, locationNodes: List<LocationNode> = listOf()) : Named {

    constructor(base: Network) : this(base.name, duplicateNodesAndConnections(base.locationNodes))

    constructor(name: String, locationRecipe: LocationRecipe) : this(name, listOf(LocationNode(name, locationName = locationRecipe.name)))

    private val locationNodes = NameSearchableList(locationNodes, LocationNode(name, isRoot = true, network = this, parent = this.name))
    val rootNode by lazy { findRootNode() }
    val rootNodeHeight by lazy { findRootNodeHeight() }

    init {
        locationNodes.forEach {
            it.network = this
        }
    }

    override fun equals(other: Any?): Boolean {
        //This isn't as precise as I'd like because we need to avoid recursive equals
        return other is Network && name == other.name && rootNode.name == other.rootNode.name && locationNodes.size == other.locationNodes.size
    }

    private fun findRootNode(): LocationNode {
        return locationNodes.firstOrNull { it.isRoot }
            ?: locationNodes.first()
    }

    private fun findRootNodeHeight(): Int {
        return rootNode.getDistanceToLowestNodeInNetwork()
    }

    fun getLocationNode(name: String): LocationNode {
        return getLocationNodeOrNull(name)
            ?: throw RuntimeException("Could not find location node $name in list ${locationNodes.joinToString { it.name }}")
    }

    fun getLocationNodeOrNull(name: String): LocationNode? {
        return locationNodes.getOrNull(name)
            ?: locationNodes.firstOrNull { cleanPathPart(it.name) == name }
    }

    fun getLocationNodes(): List<LocationNode> {
        return locationNodes.toList()
    }

    fun hasLocation(name: String): Boolean {
        return locationNodes.exists(name)
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

    fun findLocations(name: String): List<LocationNode> {
        return locationNodes.getAll(name)
    }

    fun countLocationNodes(): Int {
        return locationNodes.size
    }

    fun locationNodeExists(name: String): Boolean {
        return locationNodes.exists(name)
    }

    private fun getFurthestLocation(direction: Direction = Direction.BELOW): LocationNode? {
        return getFurthestLocations(direction)
            .maxByOrNull { rootNode.getDistanceTo(it) }
    }

    fun getFurthestLocations(direction: Direction = Direction.BELOW): List<LocationNode> {
        val bottomNodes = mutableListOf<LocationNode>()
        val inverted = direction.invert()

        locationNodes.forEach { node ->
            if (node.getNeighborsInGeneralDirection(inverted).isNotEmpty() && node.getNeighborsInGeneralDirection(direction).isEmpty()) {
                bottomNodes.add(node)
            }
        }

        return bottomNodes
    }

    fun getSize(): Vector {
        val root = rootNode

        val x = getFarthestDistanceInDirection(root, Direction.EAST) + getFarthestDistanceInDirection(root, Direction.WEST)
        val y = getFarthestDistanceInDirection(root, Direction.NORTH) + getFarthestDistanceInDirection(root, Direction.SOUTH)
        val z = getFarthestDistanceInDirection(root, Direction.ABOVE) + getFarthestDistanceInDirection(root, Direction.BELOW)

        return Vector(x, y, z)
    }

    private fun getFarthestDistanceInDirection(node: LocationNode, direction: Direction = Direction.BELOW): Int {
        return node.getDistanceTo(getFurthestLocation(direction))
    }

    fun addLocationNode(locationNode: LocationNode) {
        locationNodes.add(locationNode)
    }

}


private fun duplicateNodesAndConnections(oldNodes: List<LocationNode>): List<LocationNode> {
    val newToOldNodes = oldNodes.associateBy { LocationNode(it) }
    val oldToNewNodes = newToOldNodes.keys.associateBy { newToOldNodes[it] }

    newToOldNodes.keys.forEach { newNode ->
        val oldNode = newToOldNodes[newNode]!!
        oldNode.getNeighborConnections().forEach { oldConnection ->
            val newSource = LocationPoint(oldToNewNodes[oldConnection.source.location]!!, oldConnection.source.vector, oldConnection.source.thingName, oldConnection.source.partName)
            val newDest = LocationPoint(oldToNewNodes[oldConnection.destination.location]!!, oldConnection.destination.vector, oldConnection.destination.thingName, oldConnection.destination.partName)
            newNode.addConnection(Connection(newSource, newDest, oldConnection.restricted))
        }
    }

    return newToOldNodes.keys.toList()
}
