package processing

import EventManager
import events.EventListener
import events.MapEvent
import gameState.Location

class MapManager {
    init {
        MapHandler(this)
    }

    class MapHandler(private val manager: MapManager) : EventListener<MapEvent> {
        override fun getPriority(): Int {
            return 0
        }

        init {
            EventManager.registerListener(this)
        }

        override fun handle(event: MapEvent) {
            when (event.type) {
                MapEvent.Type.INFO -> manager.getInfo(event.target)
                MapEvent.Type.CHILDREN -> manager.getChildren(event.target)
                MapEvent.Type.SIBLINGS -> manager.getSiblings(event.target)
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