package processing

import events.EventListener
import events.MapEvent
import gameState.Location

object MapManager {

    class MapHandler : EventListener<MapEvent>() {
        override fun handle(event: MapEvent) {
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
        if (target.locations.isNotEmpty()) {
            val children = target.locations.joinToString(", ")
            println("${target.name} is made up of $children")
        } else {
            println("${target.name} doesn't have any smaller locations")
        }
    }

    private fun getSiblings(target: Location) {
        if (target.getParent() != target && target.getParent().locations.isNotEmpty()) {
            val siblings = target.getParent().locations.filter{it != target}.joinToString(", ")
            println("${target.name} is neighbored by $siblings")
        } else {
            println("${target.name} has no known neighbors")
        }
    }
}