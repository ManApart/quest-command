package system.location

import core.gameState.GameState
import core.gameState.location.*
import core.utility.NameSearchableList
import system.DependencyInjector

object LocationManager {
    private var parser = DependencyInjector.getImplementation(LocationParser::class.java)
    private var locations = parser.loadLocations()
    private var networks = loadNetworks()


    private fun loadNetworks(): NameSearchableList<Network> {
        val nodes: List<LocationNode> = parser.loadLocationNodes()

        val nodeMap = buildInitialMap(nodes)
        createNeighborsAndNeighborLinks(nodeMap)
        createLocationIfNeeded(nodeMap)

        val networks = nodeMap.map { entry ->
            Network(entry.key, entry.value)
        }

        return NameSearchableList(networks)
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
                node.protoLocationLinks.forEach { link ->
                    var neighbor = getLocationNodeByExactName(link.name, network)
                    if (neighbor == null) {
                        neighbor = LocationNode(link.name, parent = networkName)
                        nodeMap[networkName]?.add(neighbor)
                    }
                    val locationLink = LocationLink(node, neighbor, link.position, link.restricted)
                    node.addLink(locationLink)

                    if (!link.oneWay) {
                        neighbor.addLink(locationLink.invert())
                    }
                }
            }
        }
    }

    private fun createLocationIfNeeded(nodeMap: Map<String, List<LocationNode>>) {
        nodeMap.values.forEach { network ->
            network.forEach { node ->
                if (!locationExists(node.locationName)) {
                    locations.add(Location(node.locationName))
                }
            }
        }
    }

    private fun getLocationNodeByExactName(name: String, nodes: List<LocationNode>): LocationNode? {
        return nodes.firstOrNull { name == it.name }
    }

    fun reset() {
        parser = DependencyInjector.getImplementation(LocationParser::class.java)
        locations = parser.loadLocations()
        networks = loadNetworks()
    }

    fun getLocation(name: String): Location {
        return locations.getOrNull(name) ?: NOWHERE
    }

    fun getLocations(): List<Location> {
        return locations.toList()
    }

    fun getNetwork(): Network {
        return getNetwork(GameState.player.location.parent)
    }

    fun getNetworks(): List<Network> {
        return networks.toList()
    }

    fun getNetwork(name: String): Network {
        return networks.getOrNull(name) ?: throw IllegalArgumentException("Network $name does not exist!")
    }

    fun locationExists(name: String): Boolean {
        return locations.exists(name)
    }


}