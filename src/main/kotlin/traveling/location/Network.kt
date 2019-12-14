package traveling.location

import traveling.direction.Direction
import traveling.direction.NO_VECTOR
import traveling.direction.Vector
import core.utility.NameSearchableList
import core.utility.Named

class Network(override val name: String, locationNodes: List<LocationNode> = listOf(), locations: List<Location> = listOf()) : Named {
    constructor(base: Network) : this(base.name, base.locationNodes, base.locations.map { Location(it) })

    private val locationNodes = NameSearchableList(locationNodes)
    private val locations = NameSearchableList(locations)
    val rootNode by lazy { findRootNode() }
    val rootNodeHeight by lazy { findRootNodeHeight() }

    private fun findRootNode(): LocationNode? {
        return locationNodes.firstOrNull { it.isRoot }
                ?: locationNodes.firstOrNull()
    }

    private fun findRootNodeHeight(): Int {
        return rootNode?.getDistanceToLowestNodeInNetwork() ?: 0
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

    fun getLocation(name: String): Location {
        return locations.getOrNull(name) ?: NOWHERE
    }

    fun getLocations(): List<Location> {
        return locations.toList()
    }

    fun locationExists(name: String): Boolean {
        return locations.exists(name)
    }

    private fun getFurthestLocation(direction: Direction = Direction.BELOW): LocationNode? {
        return if (rootNode == null) {
            null
        } else {
            getFurthestLocations(direction)
                    .maxBy { (rootNode as LocationNode).getDistanceTo(it) }
        }
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
        return if (rootNode == null) {
            NO_VECTOR
        } else {
            val root = rootNode as LocationNode

            val x = getFarthestDistanceInDirection(root, Direction.EAST) + getFarthestDistanceInDirection(root, Direction.WEST)
            val y = getFarthestDistanceInDirection(root, Direction.NORTH) + getFarthestDistanceInDirection(root, Direction.SOUTH)
            val z = getFarthestDistanceInDirection(root, Direction.ABOVE) + getFarthestDistanceInDirection(root, Direction.BELOW)

            Vector(x, y, z)
        }
    }

    private fun getFarthestDistanceInDirection(node: LocationNode, direction: Direction = Direction.BELOW): Int {
        return node.getDistanceTo(getFurthestLocation(direction))
    }


}