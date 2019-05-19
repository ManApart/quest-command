package system.location

import core.gameState.location.Connection
import core.gameState.location.LocationNode
import core.gameState.location.LocationPoint

class LocationHelper {

    fun buildInitialMap(nodes: List<LocationNode>): HashMap<String, MutableList<LocationNode>> {
        val nodeMap = HashMap<String, MutableList<LocationNode>>()

        nodes.forEach { node ->
            nodeMap.putIfAbsent(node.parent, mutableListOf())
            nodeMap[node.parent]?.add(node)
        }
        return nodeMap
    }

    fun createNeighborsAndNeighborLinks(nodeMap: Map<String, MutableList<LocationNode>>) {
        nodeMap.keys.forEach { networkName ->
            val network = nodeMap[networkName]?.toList() ?: listOf()
            network.forEach { node ->
                node.protoConnections.forEach { link ->
                    var neighbor = getLocationNodeByExactName(link.connection.location, network)
                    if (neighbor == null) {
                        neighbor = LocationNode(link.connection.location, parent = networkName)
                        nodeMap[networkName]?.add(neighbor)
                    }
                    val locationLink = Connection(LocationPoint(node), LocationPoint(neighbor), link.vector, link.restricted)
                    node.addConnection(locationLink)

                    if (!link.oneWay) {
                        neighbor.addConnection(locationLink.invert())
                    }
                }
            }
        }
    }

    private fun getLocationNodeByExactName(name: String, nodes: List<LocationNode>): LocationNode? {
        return nodes.firstOrNull { name == it.name }
    }

}