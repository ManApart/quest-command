package gameState.location

import traveling.direction.Direction
import traveling.location.*
import traveling.location.location.LocationRecipe
import traveling.location.location.LocationNode
import traveling.location.location.LocationPoint

class LocationHelper {

    fun createNetwork(depth: Int): Network {
        val source = LocationNode("Source")
        val locationNodes = addNeighbors(source, depth)
        val locations = locationNodes.map { LocationRecipe(it.locationName) }

        return Network("Network", locationNodes, locations)
    }

    fun createLocations(depth: Int): LocationNode {
        val source = LocationNode("Source", parent = "Network")
        addNeighbors(source, depth)
        return source
    }

    private fun addNeighbors(source: LocationNode, depth: Int): MutableList<LocationNode> {
        val neighbors = mutableListOf(source)
        listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.ABOVE, Direction.BELOW)
                .forEach { direction ->
                    val neighbor = LocationNode(direction.toString())
                    neighbors.add(neighbor)
                    val link = Connection(LocationPoint(source), LocationPoint(neighbor), direction.vector)
                    source.addConnection(link)
                    neighbor.addConnection(link.invert())
                    createLocations(neighbor, direction, depth - 1, depth, neighbors)
                }
        return neighbors
    }

    private fun createLocations(source: LocationNode, direction: Direction, depth: Int, totalDepth: Int, neighbors: MutableList<LocationNode>) {
        if (depth <= 0) {
            return
        }
        val neighbor = LocationNode(direction.toString() + (totalDepth - depth))
        neighbors.add(neighbor)

        val link = Connection(LocationPoint(source), LocationPoint(neighbor), direction.vector)
        source.addConnection(link)
        neighbor.addConnection(link.invert())

        createLocations(neighbor, direction, depth - 1, totalDepth, neighbors)
    }
}