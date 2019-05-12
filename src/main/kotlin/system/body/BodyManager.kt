package system.body

import core.gameState.body.Body
import core.gameState.body.NONE
import core.gameState.location.*
import core.utility.NameSearchableList
import system.DependencyInjector

object BodyManager {
    private var parser = DependencyInjector.getImplementation(BodyParser::class.java)
    private var bodies = createBodies()

    fun reset() {
        parser = DependencyInjector.getImplementation(BodyParser::class.java)
        bodies = createBodies()
    }

    private fun createBodies(): NameSearchableList<Body> {
        val nodes: List<LocationNode> = parser.loadBodies()
        val bodyParts = parser.loadBodyParts()

        val nodeMap = buildInitialMap(nodes)
        createNeighborsAndNeighborLinks(nodeMap)
        val locations = NameSearchableList(bodyParts.map { Location(it.name, bodyPart = it) })

        val networks = nodeMap.map { entry ->
            val networkLocations = entry.value.map { locations.get(it.locationName) }
            Network(entry.key, entry.value, networkLocations)
        }

        val bodies = (networks.map { Body(it.name, it) }).plus(NONE)

        return NameSearchableList(bodies)
    }

    private fun buildInitialMap(nodes: List<LocationNode>): HashMap<String, MutableList<LocationNode>> {
        val nodeMap = HashMap<String, MutableList<LocationNode>>()

        nodes.forEach { node ->
            nodeMap.putIfAbsent(node.parent, mutableListOf())
            nodeMap[node.parent]?.add(node)
        }
        return nodeMap
    }

    private fun createNeighborsAndNeighborLinks(nodeMap: Map<String, MutableList<LocationNode>>) {
        nodeMap.keys.forEach { networkName ->
            val network = nodeMap[networkName]?.toList() ?: listOf()
            network.forEach { node ->
                node.protoConnections.forEach { link ->
                    var neighbor = getLocationNodeByExactName(link.name, network)
                    if (neighbor == null) {
                        neighbor = LocationNode(link.name, parent = networkName)
                        nodeMap[networkName]?.add(neighbor)
                    }
                    val locationLink = Connection(LocationPoint(node), LocationPoint(neighbor), link.position, link.restricted)
                    node.addLink(locationLink)

                    if (!link.oneWay) {
                        neighbor.addLink(locationLink.invert())
                    }
                }
            }
        }
    }

    private fun getLocationNodeByExactName(name: String, nodes: List<LocationNode>): LocationNode? {
        return nodes.firstOrNull { name == it.name }
    }

    fun bodyExists(name: String): Boolean {
        return bodies.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } != null
    }

    fun getBody(name: String): Body {
        return bodies.get(name)
    }
}