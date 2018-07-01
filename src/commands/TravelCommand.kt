package commands

import events.TravelStartEvent
import gameState.GameState

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
        //TODO  - append player location
        val found = GameState.world.findLocation(args)
        val argPath = "world " + args.joinToString(" ")
        val foundPath = found.getPath().joinToString(" ").toLowerCase()

        if (foundPath == argPath) {
            EventManager.postEvent(TravelStartEvent(GameState.player.location, found))
        } else {
            println("Found $foundPath instead of $argPath")
        }

    }
}