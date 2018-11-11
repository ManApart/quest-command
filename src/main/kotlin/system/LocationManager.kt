package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.location.Location
import core.gameState.location.LocationLink
import core.gameState.location.LocationNode
import core.utility.NameSearchableList

object LocationManager {
    private val locations = loadLocations()
    private val locationNodes = loadLocationNodes()

    private fun loadLocations(): NameSearchableList<Location> {
        val json = this::class.java.getResourceAsStream("/data/location/Locations.json")
        return jacksonObjectMapper().readValue(json)
    }

    private fun loadLocationNodes(): NameSearchableList<LocationNode> {
        val json = this::class.java.getResourceAsStream("/data/location/LocationLinks.json")
        val nodes: List<LocationNode> = jacksonObjectMapper().readValue(json)

        val locationLinks = NameSearchableList(nodes)
        nodes.forEach { node ->
            node.getNeighborLinks().forEach { link ->
                val neighbor = getLocationNodeByExactName(link.name, locationLinks)
                if (neighbor == null) {
                    val newNeighbor = LocationNode(link.name)
                    locationLinks.add(newNeighbor)
                    newNeighbor.addLink(LocationLink(node.name, link.position.invert()))
                } else {
                    neighbor.addLink(LocationLink(node.name, link.position.invert()))
                }
            }
        }

        return locationLinks
    }

    private fun getLocationNodeByExactName(name: String, nodes: List<LocationNode>): LocationNode? {
        return nodes.firstOrNull { name == it.name }
    }

    fun getLocation(name: String): Location {
        return locations.get(name)
    }

    fun getLocationNode(name: String): LocationNode {
        return locationNodes.get(name)
    }

//    //TODO - test
//    fun findLeastDistant(locations: List<LocationNode>) : LocationNode {
//        return locations.sortedBy { position.getDistance(it.position) }.first()
//    }
//
//    fun findLocation(args: List<String>): LocationNode {
//        return if (args.isEmpty()) {
//            this
//        } else {
//            val adjustedArgs = args.flatMap { it.toLowerCase().split(" ") }
//            return findChildLocation(adjustedArgs)
//        }
//    }
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