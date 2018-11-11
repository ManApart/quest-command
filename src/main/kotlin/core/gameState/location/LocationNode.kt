package core.gameState.location

import core.gameState.Direction
import core.utility.Named
import system.LocationManager

class LocationNode(override val name: String, private val locationName: String = name, private val locations: MutableList<LocationLink> = mutableListOf()) : Named {

    override fun toString(): String {
        return name
    }

    fun getDescription(): String {
        return name
    }

    fun addLink(link: LocationLink) {
        locations.add(link)
    }

    fun getNeighborLinks(): List<LocationLink> {
        return locations.toList()
    }

    fun getNeighbors(direction: Direction): List<LocationNode> {
        val links = locations.filter {
            it.position.getDirection() == direction
        }
        return links.map {
            LocationManager.getLocationNode(it.name)
        }
    }

    fun getLocation(): Location {
        return LocationManager.getLocation(locationName)
    }

//    fun getPathTo(destination: LocationNode): List<LocationNode> {
//
//    }
//
//    fun getDistanceTo(destination: LocationNode): List<LocationNode> {
//
//    }
//
//    private fun getDistanceOfPath(path: List<LocationNode>) : Int {
//
//    }


}