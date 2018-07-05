package commands

import events.TravelStartEvent
import gameState.GameState
import gameState.Location
import processing.EventManager

class TravelCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Travel", "t")
    }

    override fun getDescription(): String {
        return "Travel:\n\tTravel to different locations."
    }

    override fun getManual(): String {
        return "\n\tTravel to <location> - Start traveling to a location." +
                "\n\tTravel - Continue traveling to a goal location. X" +
                "\n\tTravel goal - Remember what the travel location goal is. X"

    }

    override fun execute(args: List<String>) {
        val found = GameState.world.findLocation(args)
        val argPath = args.joinToString(" ")
        val foundPath = pathAsString(found)

        if (foundPath == argPath) {
            EventManager.postEvent(TravelStartEvent(GameState.player.location, found))
        } else {
            val args2 = (pathAsString(GameState.player.location) + " " + argPath).split(" ")
            val found2 = GameState.world.findLocation(args2)
            val argPath2 = args2.joinToString(" ")
            val foundPath2 = pathAsString(found2)

            if (foundPath2 == argPath2) {
                EventManager.postEvent(TravelStartEvent(GameState.player.location, found2))
            } else {
                println("Found $foundPath instead of $argPath")
            }
        }

    }

    private fun pathAsString(location: Location) : String {
        val path = location.getPath()
        val trimmed = if (path.size > 1 && path[0].toLowerCase() == "world") path.subList(1,path.size) else path
        return trimmed.joinToString(" ").toLowerCase()
    }

}