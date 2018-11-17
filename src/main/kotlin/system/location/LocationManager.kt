package system.location

import core.gameState.location.Location
import core.gameState.location.LocationLink
import core.gameState.location.LocationNode
import core.utility.NameSearchableList
import system.DependencyInjector

object LocationManager {
    val NOWHERE = Location("Nowhere")
    val NOWHERE_NODE = LocationNode("Nowhere")

    private var parser = DependencyInjector.getImplementation(LocationParser::class.java)

    private var locations = parser.loadLocations()
    private var locationNodes = loadLocationNodes()

    private fun loadLocationNodes(): NameSearchableList<LocationNode> {
        val nodes: List<LocationNode> = parser.loadLocationNodes()

        val locationNodes = NameSearchableList(nodes)
        nodes.forEach { node ->
            node.getNeighborLinks().forEach { link ->
                val neighbor = getLocationNodeByExactName(link.name, locationNodes)
                if (neighbor == null) {
                    val newNeighbor = LocationNode(link.name)
                    locationNodes.add(newNeighbor)
                    newNeighbor.addLink(LocationLink(node.name, link.position.invert()))
                } else {
                    neighbor.addLink(LocationLink(node.name, link.position.invert()))
                }
            }
        }

        return locationNodes
    }

    private fun getLocationNodeByExactName(name: String, nodes: List<LocationNode>): LocationNode? {
        return nodes.firstOrNull { name == it.name }
    }

    fun reload() {
        parser = DependencyInjector.getImplementation(LocationParser::class.java)
        parser.loadLocations()
        locationNodes = loadLocationNodes()
    }

    fun getLocation(name: String): Location {
        return locations.get(name)
    }

    fun getLocationNode(name: String): LocationNode {
        return locationNodes.get(name)
    }

    fun findLocation(name: String): LocationNode {
        return if (locationNodes.exists(name)) {
            locationNodes.get(name)
        } else {
            NOWHERE_NODE
        }
    }

    fun countLocationNodes() : Int {
        return locationNodes.size
    }


//    //TODO - test
//    fun findLeastDistant(locations: List<LocationNode>) : LocationNode {
//        return locations.sortedBy { position.getDistance(it.position) }.first()
//    }
//

//
//    private fun findOverlap(name: String, args: List<String>): Int {
//        var wordCount = 0
//        var remainingWords = name.toLowerCase()
//        for (i in 0 until args.size) {
//            when {
//                remainingWords.isBlank() -> return wordCount
//                remainingWords.contains(args[i]) -> {
//                    remainingWords = remainingWords.substring(remainingWords.indexOf(args[i]))
//                    wordCount++
//                }
//                else -> return wordCount
//            }
//        }
//
//        return wordCount
//    }

}