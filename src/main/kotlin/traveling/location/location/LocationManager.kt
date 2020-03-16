package traveling.location.location

import core.DependencyInjector
import core.GameState
import core.utility.NameSearchableList
import traveling.location.Network

object LocationManager {
    private const val locationNodePath = "/data/generated/content/location/location-nodes"
    private const val locationPath = "/data/generated/content/location/locations"

    private val locationHelper = LocationHelper()
    private var networks = loadNetworks()

    fun reset() {
        networks = loadNetworks()
    }

    private fun loadNetworks(): NameSearchableList<Network> {
        val parser = DependencyInjector.getImplementation(LocationParser::class.java)
        val locations = parser.loadLocations(locationPath)
        val nodes: List<LocationNode> = parser.loadLocationNodes(locationNodePath)


        val nodeMap = locationHelper.buildInitialMap(nodes)
        locationHelper.createNeighborsAndNeighborLinks(nodeMap)
        createLocationIfNeeded(nodeMap, locations)

        val networks = nodeMap.map { entry ->
            val networkLocations = entry.value.map { locations.get(it.locationName) }.distinct()

            val network = Network(entry.key, entry.value, networkLocations)
            entry.value.forEach { it.network = network }
            network
        }
        DEFAULT_NETWORK.addLocationNode(NOWHERE_NODE)
        return NameSearchableList(networks + DEFAULT_NETWORK)
    }

    private fun createLocationIfNeeded(nodeMap: Map<String, List<LocationNode>>, locationRecipes: NameSearchableList<LocationRecipe>) {
        nodeMap.values.forEach { network ->
            network.forEach { node ->
                if (!locationRecipes.exists(node.locationName)) {
                    locationRecipes.add(LocationRecipe(node.locationName))
                }
            }
        }
    }

    fun networkExists(name: String = GameState.player.location.parent): Boolean {
        return networks.getOrNull(name) != null
    }

    fun getNetwork(name: String = GameState.player.location.parent): Network {
        return networks.getOrNull(name) ?: throw IllegalArgumentException("Network $name does not exist!")
    }

    fun getNetworks(): List<Network> {
        return networks.toList()
    }

    fun findLocationInAnyNetwork(name: String): LocationNode? {
        val network = getNetwork()
        var target = findTarget(name, network)
        var i = 0
        while (target == null && i < getNetworks().size) {
            target = findTarget(name, getNetworks()[i])
            i++
        }
        return target
    }

    private fun findTarget(name: String, network: Network): LocationNode? {
        return if (network.locationExists(name)) {
            network.findLocation(name)
        } else {
            null
        }
    }


}