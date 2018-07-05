package processing

import events.EventListener
import events.MapEvent
import gameState.Location

object MapManager {

    class MapHandler : EventListener<MapEvent>() {
        override fun execute(event: MapEvent) {
            when (event.type) {
                MapEvent.Type.INFO -> MapManager.getInfo(event.target)
                MapEvent.Type.CHILDREN -> MapManager.getChildren(event.target)
                MapEvent.Type.SIBLINGS -> MapManager.getSiblings(event.target)
            }
        }
    }

    private fun getInfo(target: Location) {
        println("You are in ${target.name}. ${target.description}")
    }

    private fun getChildren(target: Location) {
        val locations = target.locations.filter { !it.restricted }
        if (locations.isNotEmpty()) {
            val children = locations.joinToString(", ")
            println("${target.name} is made up of $children")
        } else {
            println("${target.name} doesn't have any known smaller locations")
        }
    }

    private fun getSiblings(target: Location) {
        val locations = target.getParent().locations.filter { !it.restricted }
        if (target.getParent() != target && locations.size > 1) {
            val siblings = locations.filter{it != target}.joinToString(", ")
            println("${target.name} is neighbored by $siblings")
        } else {
            println("${target.name} has no known neighbors")
        }
    }
}