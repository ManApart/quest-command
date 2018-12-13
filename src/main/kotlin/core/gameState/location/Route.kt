package core.gameState.location

import core.gameState.Position
import java.lang.IllegalArgumentException


class Route(val source: LocationNode, private val links: MutableList<LocationLink> = mutableListOf()) {

    constructor(base: Route) : this(base.source) {
        base.getLinks().forEach {
            addLink(it)
        }
    }

    var destination: LocationNode = source
    val position: Position = Position()

    override fun toString(): String {
        return "Route : ${source.name} - ${destination.name}; Steps: ${getLinks().size}"
    }

    fun addLink(link: LocationLink) {
        if (link.source != destination){
            throw IllegalArgumentException("Route starting with '${source.name}' was passed a link whose source '${link.source.name}' did not match current destination '${destination.name}'.")
        }
        links.add(link)
        destination = link.destination
    }

    fun getLinks() : List<LocationLink> {
        return links.toList()
    }

    fun getDistance() : Int {
        return links.asSequence().map{ it.position.getDistance() }.sum()
    }

}