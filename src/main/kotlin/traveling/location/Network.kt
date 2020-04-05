package traveling.location

import core.utility.NameSearchableList
import core.utility.Named
import traveling.direction.Direction
import traveling.direction.Vector
import traveling.location.location.LocationRecipe
import traveling.location.location.LocationNode
import traveling.location.location.NOWHERE
import traveling.location.location.NOWHERE_NODE

class Network(override val name: String, locationNodes: List<LocationNode> = listOf(), locationRecipes: List<LocationRecipe> = listOf()) : Named {
    constructor(base: Network) : this(base.name, base.locationNodes, base.locations.map { LocationRecipe(it) })

    private val locationNodes = NameSearchableList(locationNodes, LocationNode("Root", isRoot = true, network = this, parent = this.name))
    private val locations = NameSearchableList(locationRecipes)
    val rootNode by lazy { findRootNode() }
    val rootNodeHeight by lazy { findRootNodeHeight() }

    private fun findRootNode(): LocationNode {
        return locationNodes.firstOrNull { it.isRoot }
                ?: locationNodes.first()
    }

    private fun findRootNodeHeight(): Int {
        return rootNode.getDistanceToLowestNodeInNetwork()
    }

    fun getLocationNode(name: String): LocationNode {
        return locationNodes.get(name)
    }

    fun getLocationNodes(): List<LocationNode> {
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

    fun getLocation(name: String): LocationRecipe {
        return locations.getOrNull(name) ?: NOWHERE
    }

    fun getLocations(): List<LocationRecipe> {
        return locations.toList()
    }

    fun locationExists(name: String): Boolean {
        return locations.exists(name)
    }

    fun locationNodeExists(name: String): Boolean {
        return locations.exists(name)
    }

    private fun getFurthestLocation(direction: Direction = Direction.BELOW): LocationNode? {
        return getFurthestLocations(direction)
                .maxBy { rootNode.getDistanceTo(it) }
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
        val root = rootNode as LocationNode

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