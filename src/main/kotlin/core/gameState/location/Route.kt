package core.gameState.location

import core.gameState.Vector
import java.lang.IllegalArgumentException


class Route(val source: LocationNode, private val links: MutableList<Connection> = mutableListOf()) {

    constructor(base: Route) : this(base.source) {
        base.getLinks().forEach {
            addLink(it)
        }
    }

    var destination: LocationNode = source
    val vector: Vector = Vector()

    override fun toString(): String {
        return "Route : ${source.name} - ${destination.name}; Steps: ${getLinks().size}"
    }

    fun addLink(link: Connection) {
        if (link.source.location != destination) {
            throw IllegalArgumentException("Route starting with '${source.name}' was passed a link whose source '${link.source.location.name}' did not match current destination '${destination.name}'.")
        }
        links.add(link)
        destination = link.destination.location
    }

    fun getLinks(): List<Connection> {
        return links.toList()
    }

    fun getDistance(): Int {
        return links.asSequence().map { it.vector.getDistance() }.sum()
    }

    fun getDirectionString(): String {
        return links.asSequence()
                .map { it.vector.getDirection().shortcut }
                .joinToString(", ")
                .toUpperCase()
    }

    fun isOnRoute(location: LocationNode): Boolean {
        return location == source || links.any { location == it.destination.location }
    }

    fun getNextStep(location: LocationNode) : Connection {
        return links.first { it.source.location == location }
    }

    fun getRouteProgressString(currentLocation: LocationNode): String {
        val names = links.asSequence()
                .map { it.source.location.name }
                .toMutableList()

        names.add(destination.name)
        return names.joinToString(", ") {
            if (it == currentLocation.name) {
                "*$it*"
            } else {
                it
            }
        }

    }

}