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
            createNeighborLinks(node, locationNodes)
            createLocationIfNeeded(node)
        }

        return locationNodes
    }

    private fun createLocationIfNeeded(node: LocationNode) {
        if (!locationExists(node.locationName)){
            locations.add(Location(node.locationName))
        }
    }

    private fun createNeighborLinks(node: LocationNode, locationNodes: NameSearchableList<LocationNode>) {
        node.protoLocationLinks.forEach { link ->
            var neighbor = getLocationNodeByExactName(link.name, locationNodes)
            if (neighbor == null) {
                neighbor = LocationNode(link.name)
                locationNodes.add(neighbor)
            }
            val locationLink = LocationLink(node, neighbor, link.position, link.restricted)
            node.addLink(locationLink)

            if (!link.oneWay) {
                neighbor.addLink(locationLink.invert())
            }
        }
    }

    private fun getLocationNodeByExactName(name: String, nodes: List<LocationNode>): LocationNode? {
        return nodes.firstOrNull { name == it.name }
    }

    fun reset() {
        parser = DependencyInjector.getImplementation(LocationParser::class.java)
        locations = parser.loadLocations()
        locationNodes = loadLocationNodes()
    }

    fun getLocation(name: String): Location {
        return locations.getOrNull(name) ?: NOWHERE
    }

    fun getLocations() : List<Location> {
        return locations.toList()
    }

    fun getLocationNode(name: String): LocationNode {
        return locationNodes.get(name)
    }

    fun getLocationNodes() : List<LocationNode> {
        return locationNodes.toList()
    }

    fun findLocation(name: String): LocationNode {
        return when {
            locationNodes.exists(name) -> locationNodes.get(name)
            name.startsWith("$") -> NOWHERE_NODE
            else -> {
                println("Could not find location: $name")
                NOWHERE_NODE
            }
        }
    }

    fun countLocationNodes(): Int {
        return locationNodes.size
    }

    fun locationExists(name: String) : Boolean {
        return locations.exists(name)
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