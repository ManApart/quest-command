package traveling.location.location

import core.utility.putAbsent
import traveling.location.Connection
import traveling.location.network.LocationNode

fun buildInitialMap(nodes: List<LocationNode>): HashMap<String, MutableList<LocationNode>> {
    val nodeMap = HashMap<String, MutableList<LocationNode>>()

    nodes.forEach { node ->
        nodeMap.putAbsent(node.parent, mutableListOf())
        nodeMap[node.parent]?.add(node)
    }
    return nodeMap
}

fun createNeighborsAndNeighborLinks(nodeMap: MutableMap<String, MutableList<LocationNode>>) {
    nodeMap.keys.forEach { networkName ->
        val network = nodeMap[networkName]?.toList() ?: listOf()
        network.forEach { node ->
            node.connectionRecipes.forEach { protoConnection ->
                val connectionNetworkName = protoConnection.connection.network ?: networkName
                if (nodeMap[connectionNetworkName] == null) {
                    nodeMap[connectionNetworkName] = mutableListOf()
                }
                val connectionNetwork = nodeMap[connectionNetworkName] ?: error("Network $connectionNetworkName does not exist, though it is referenced by location ${node.name}")
                var neighbor = getLocationNodeByExactName(protoConnection.connection.location, connectionNetwork)

                if (neighbor == null) {
                    neighbor = LocationNode(protoConnection.connection.location, parent = connectionNetworkName)
                    connectionNetwork.add(neighbor)
                }

                val originPoint = LocationPoint(node, protoConnection.originPoint, protoConnection.thing, protoConnection.part)
                val destinationPoint = LocationPoint(neighbor, protoConnection.destinationPoint, protoConnection.connection.thing, protoConnection.connection.part)
                val locationLink = Connection(originPoint, destinationPoint, protoConnection.restricted, protoConnection.hidden)
                node.addConnection(locationLink)

                if (!protoConnection.oneWay) {
                    neighbor.addConnection(locationLink.invert())
                }
            }
        }
    }
}

private fun getLocationNodeByExactName(name: String, nodes: List<LocationNode>): LocationNode? {
    return nodes.firstOrNull { name == it.name }
}
