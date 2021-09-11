package traveling.location.location

import core.DependencyInjector
import core.GameState
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import traveling.location.Network
import traveling.location.network.*

object LocationManager {
    private val locationHelper = LocationHelper()
    private var networks = loadNetworks()

    fun reset() {
        networks = loadNetworks()
    }

    private fun loadNetworks(): NameSearchableList<Network> {
        val nodeCollection = DependencyInjector.getImplementation(NetworksCollection::class.java)
        val locationCollection = DependencyInjector.getImplementation(LocationsCollection::class.java)
        val locations = locationCollection.values.build().toNameSearchableList()
        val nodes = nodeCollection.values.build()


        val nodeMap = locationHelper.buildInitialMap(nodes)
        locationHelper.createNeighborsAndNeighborLinks(nodeMap)
        createLocationIfNeeded(nodeMap, locations)

        val networks = nodeMap.map { entry ->
            val networkLocations = entry.value.map { locations.get(it.locationName) }.distinct()

            val network = Network(entry.key, entry.value, networkLocations)
            entry.value.forEach { it.network = network }
            network
        }
        if (!DEFAULT_NETWORK.locationNodeExists(NOWHERE_NODE.name)) {
            DEFAULT_NETWORK.addLocationNode(NOWHERE_NODE)
        }
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

    fun findLocationsInAnyNetwork(name: String): List<LocationNode> {
        return getNetworks().flatMap { it.findLocations(name) }
    }

    private fun findTarget(name: String, network: Network): LocationNode? {
        return if (network.locationRecipeExists(name)) {
            network.findLocation(name)
        } else {
            null
        }
    }


}