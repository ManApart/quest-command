package traveling.location.location

import core.DependencyInjector
import core.startupLog
import core.thing.Thing
import core.utility.NameSearchableList
import core.utility.lazyM
import core.utility.toNameSearchableList
import traveling.location.Network
import traveling.location.network.*

object LocationManager {
    private var networks by lazyM { loadNetworks() }

    fun reset() {
        networks = loadNetworks()
    }

    private fun loadNetworks(): NameSearchableList<Network> {
        startupLog("Loading Networks.")
        val nodeCollection = DependencyInjector.getImplementation(NetworksCollection::class)
        val locationCollection = DependencyInjector.getImplementation(LocationsCollection::class)
        val locations = locationCollection.values.build().toNameSearchableList()
        val nodes = nodeCollection.values.build()


        val nodeMap = buildInitialMap(nodes)
        createNeighborsAndNeighborLinks(nodeMap)
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

    fun networkExists(name: String): Boolean {
        return networks.getOrNull(name) != null
    }

    fun getNetwork(name: String): Network {
        return networks.getOrNull(name) ?: throw IllegalArgumentException("Network $name does not exist!")
    }

    fun getNetworks(): List<Network> {
        return networks.toList()
    }

    fun findLocationInAnyNetwork(source: Thing, name: String): LocationNode? {
        val network = getNetwork(source.location.parent)
        var location = findLocation(name, network)
        var i = 0
        while (location == null && i < getNetworks().size) {
            location = findLocation(name, getNetworks()[i])
            i++
        }
        return location
    }

    fun findLocationsInAnyNetwork(name: String): List<LocationNode> {
        return getNetworks().flatMap { it.findLocations(name) }
    }

    private fun findLocation(name: String, network: Network): LocationNode? {
        return if (network.locationRecipeExists(name)) {
            network.findLocation(name)
        } else {
            null
        }
    }


}