package core.body

import core.DependencyInjector
import core.utility.NameSearchableList
import traveling.location.Network
import traveling.location.location.LocationHelper
import traveling.location.location.LocationRecipe
import traveling.location.location.build
import traveling.location.network.LocationNode
import traveling.location.network.build

object BodyManager {
    private val locationHelper = LocationHelper()
    private var bodies = createBodies()

    fun reset() {
        bodies = createBodies()
    }

    private fun createBodies(): NameSearchableList<Body> {
        val bodyCollection = DependencyInjector.getImplementation(BodysCollection::class.java)
        val bodyPartCollection = DependencyInjector.getImplementation(BodyPartsCollection::class.java)
        val nodes = bodyCollection.values.build()
        val bodyParts = bodyPartCollection.values.build()

        val nodeMap = locationHelper.buildInitialMap(nodes)
        locationHelper.createNeighborsAndNeighborLinks(nodeMap)
        val locations = createLocations(bodyParts, nodeMap.values.flatten())

        val networks = nodeMap.map { entry ->
            val networkLocations = entry.value.map { locations.get(it.locationName) }.distinct()
            val network = Network(entry.key, entry.value, networkLocations)
            entry.value.forEach { it.network = network }
            network
        }

        val bodies = (networks.map { Body(it.name, it) }).plus(NONE)

        return NameSearchableList(bodies)
    }

    private fun createLocations(bodyParts: List<LocationRecipe>, locationNodes: List<LocationNode>) : NameSearchableList<LocationRecipe> {
        val locations = NameSearchableList(bodyParts)

        locationNodes.forEach { node ->
            if (locations.getOrNull(node.locationName) == null){
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