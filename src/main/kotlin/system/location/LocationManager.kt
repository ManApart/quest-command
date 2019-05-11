package system.location

import core.gameState.GameState
import core.gameState.location.Location
import core.gameState.location.LocationLink
import core.gameState.location.LocationNode
import core.gameState.location.Network
import core.utility.NameSearchableList
import system.DependencyInjector

object LocationManager {
    private var networks = loadNetworks()

    fun reset() {
        networks = loadNetworks()
    }

    private fun loadNetworks(): NameSearchableList<Network> {
        val parser = DependencyInjector.getImplementation(LocationParser::class.java)
        val locations = parser.loadLocations()
        val nodes: List<LocationNode> = parser.loadLocationNodes()

        val nodeMap = buildInitialMap(nodes)
        createNeighborsAndNeighborLinks(nodeMap)
        createLocationIfNeeded(nodeMap, locations)

        val networks = nodeMap.map { entry ->
            val networkLocations = entry.value.map { locations.get(it.locationName) }
            Network(entry.key, entry.value, networkLocations)
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

    private fun createLocationIfNeeded(nodeMap: Map<String, List<LocationNode>>, locations: NameSearchableList<Location>) {
        nodeMap.values.forEach { network ->
            network.forEach { node ->
                if (!locations.exists(node.locationName)) {
                    locations.add(Location(node.locationName))
                }
            }
        }
    }

    private fun getLocationNodeByExactName(name: String, nodes: List<LocationNode>): LocationNode? {
        return nodes.firstOrNull { name == it.name }
    }

    fun getNetwork(name: String = GameState.player.location.parent): Network {
        return networks.getOrNull(name) ?: throw IllegalArgumentException("Network $name does not exist!")
    }

    fun getNetworks(): List<Network> {
        return networks.toList()
    }




}