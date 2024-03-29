package core.body

import building.ModManager
import core.DependencyInjector
import core.startupLog
import core.utility.NameSearchableList
import core.utility.lazyM
import crafting.material.MaterialManager
import traveling.location.Network
import traveling.location.location.build
import traveling.location.location.buildInitialMap
import traveling.location.location.createNeighborsAndNeighborLinks
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
        val bodyParts = (bodyPartCollection.values + ModManager.bodyParts).build().associateBy { it.name }
        val nodes = (bodyCollection.values + ModManager.bodies).build(bodyParts)

        val nodeMap = buildInitialMap(nodes)
        createNeighborsAndNeighborLinks(nodeMap, bodyParts)

        val networks = nodeMap.map { (name, nodes) ->
            val network = Network(name, nodes)
            nodes.forEach { it.network = network }
            network
        }

        val bodies = networks.map { Body(it.name, MaterialManager.getMaterial(it.rootNode.recipe.material), it) }.plus(NONE)

        return NameSearchableList(bodies)
    }

    fun bodyExists(name: String): Boolean {
        return bodies.firstOrNull { it.name.lowercase() == name.lowercase() } != null
    }

    fun getBody(name: String): Body {
        return Body(bodies.get(name))
    }
}