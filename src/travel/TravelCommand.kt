package travel

import core.commands.Command
import core.commands.removeExcludedWords
import core.gameState.GameState
import system.EventManager

class TravelCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Travel", "t", "go")
    }

    override fun getDescription(): String {
        return "Travel:\n\tTravel to different locations."
    }

    override fun getManual(): String {
        return "\n\tTravel to <location> - Start traveling to a location." +
                "\n\tTravel - Continue traveling to a goal location. X" +
                "\n\tTravel goal - Remember what the travel location goal is. X"
    }

    override fun getCategory(): List<String> {
        return listOf("Travel")
    }

    override fun execute(args: List<String>) {
        val cleanedArgs = removeExcludedWords(args, listOf("to"))
        val found = LocationParsing.findLocation(GameState.player.location, cleanedArgs)

        if (found != GameState.world) {
            EventManager.postEvent(TravelStartEvent(destination = found))
        }else {
            println("Could not find ${args.joinToString(" ")}")
        }
    }

}