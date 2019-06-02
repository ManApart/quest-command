package system.body

import core.gameState.body.Body
import core.gameState.body.BodyPart
import core.gameState.body.NONE
import core.gameState.location.*
import core.utility.NameSearchableList
import system.DependencyInjector
import system.location.LocationHelper

object BodyManager {
    private val locationHelper = LocationHelper()
    private var parser = DependencyInjector.getImplementation(BodyParser::class.java)
    private var bodies = createBodies()

    fun reset() {
        parser = DependencyInjector.getImplementation(BodyParser::class.java)
        bodies = createBodies()
    }

    private fun createBodies(): NameSearchableList<Body> {
        val nodes: List<LocationNode> = parser.loadBodies()
        val bodyParts = parser.loadBodyParts()

        val nodeMap = locationHelper.buildInitialMap(nodes)
        locationHelper.createNeighborsAndNeighborLinks(nodeMap)
        val locations = createLocations(bodyParts, nodeMap.values.flatten())

        val networks = nodeMap.map { entry ->
            val networkLocations = entry.value.map { locations.get(it.locationName) }
            val network = Network(entry.key, entry.value, networkLocations)
            entry.value.forEach { it.network = network }
            network
        }

        val bodies = (networks.map { Body(it.name, it) }).plus(NONE)

        return NameSearchableList(bodies)
    }

    private fun createLocations(bodyParts: List<BodyPart>, locationNodes: List<LocationNode>) : NameSearchableList<Location> {
        val locations = NameSearchableList(bodyParts.map { Location(it.name, bodyPart = it) })

        locationNodes.forEach { node ->
            if (locations.getOrNull(node.locationName) == null){
                locations.add(Location(node.locationName, bodyPart = BodyPart(node.locationName)))
            }
        }

        return locations
    }

    fun bodyExists(name: String): Boolean {
        return bodies.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } != null
    }

    fun getBody(name: String): Body {
        return bodies.get(name)
    }
}