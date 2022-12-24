package core.body

import core.DependencyInjector
import core.startupLog
import core.utility.NameSearchableList
import core.utility.lazyM
import crafting.material.MaterialManager
import traveling.location.Network
import traveling.location.location.LocationRecipe
import traveling.location.location.build
import traveling.location.location.buildInitialMap
import traveling.location.location.createNeighborsAndNeighborLinks
import traveling.location.network.LocationNode
import traveling.location.network.build

object BodyManager {
    private var bodies by lazyM { createBodies() }

    fun reset() {
        bodies = createBodies()
    }

    private fun createBodies(): NameSearchableList<Body> {
        startupLog("Creating Bodies.")
        val bodyCollection = DependencyInjector.getImplementation(BodysCollection::class)
        val bodyPartCollection = DependencyInjector.getImplementation(BodyPartsCollection::class)
        val nodes = bodyCollection.values.build()
        val bodyParts = bodyPartCollection.values.build()

        val nodeMap = buildInitialMap(nodes)
        createNeighborsAndNeighborLinks(nodeMap)
        val locations = createLocations(bodyParts, nodeMap.values.flatten())

        val networks = nodeMap.map { entry ->
            val networkLocations = entry.value.map { locations.get(it.locationName) }.distinct()
            val network = Network(entry.key, entry.value, networkLocations)
            entry.value.forEach { it.network = network }
            network
        }

        val bodies = (networks.map { Body(it.name, MaterialManager.getMaterial(it.rootNode.getLocationRecipe().material), it) }).plus(NONE)

        return NameSearchableList(bodies)
    }

    private fun createLocations(bodyParts: List<LocationRecipe>, locationNodes: List<LocationNode>): NameSearchableList<LocationRecipe> {
        val locations = NameSearchableList(bodyParts)

        locationNodes.forEach { node ->
            if (locations.getOrNull(node.locationName) == null) {
                locations.add(LocationRecipe(node.locationName))
            }
        }

        return locations
    }

    fun bodyExists(name: String): Boolean {
        return bodies.firstOrNull { it.name.lowercase() == name.lowercase() } != null
    }

    fun getBody(name: String): Body {
        return Body(bodies.get(name))
    }
}