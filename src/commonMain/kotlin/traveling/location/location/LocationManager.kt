package traveling.location.location

import building.ModManager
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
        val locations = (locationCollection.values + ModManager.locations).build().toNameSearchableList().associateBy { it.name }
        val nodes = (nodeCollection.values + ModManager.networks).build(locations)

        val nodeMap = buildInitialMap(nodes)
        createNeighborsAndNeighborLinks(nodeMap, locations)

        val networks = nodeMap.map { (name, nodes) ->
            val network = Network(name, nodes)
            nodes.forEach { it.network = network }
            network
        }
        if (!DEFAULT_NETWORK.locationNodeExists(NOWHERE_NODE.name)) {
            DEFAULT_NETWORK.addLocationNode(NOWHERE_NODE)
        }
        return NameSearchableList(networks + DEFAULT_NETWORK)
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
        return if (network.getLocationNodes().map { it.recipe }.toNameSearchableList().exists(name)) {
            network.findLocation(name)
        } else {
            null
        }
    }


}