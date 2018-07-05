package commands

import commands.parsing.LocationParsing
import events.TravelStartEvent
import gameState.GameState
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
        val found = LocationParsing.findLocation(GameState.player.location, args)

        if (found != GameState.world) {
            EventManager.postEvent(TravelStartEvent(GameState.player.location, found))
        }else {
            println("Could not find ${args.joinToString(" ")}")
        }
    }

}