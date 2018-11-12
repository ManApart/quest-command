package travel

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.gameState.GameState
import core.gameState.location.LocationNode
import system.EventManager
import system.LocationManager

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

    override fun execute(keyword: String, args: List<String>) {
        if (CommandParser.getCommand<TravelInDirectionCommand>().getAliases().map { it.toLowerCase() }.contains(args[0].toLowerCase())) {
            CommandParser.parseCommand(args.joinToString(" "))
        } else {
            val arguments = Args(args, excludedWords = listOf("to"))
            val found = LocationManager.findLocation(arguments.argGroups[0].joinToString(" "))

            if (foundMatch(arguments.argGroups[0], found)) {
                EventManager.postEvent(TravelStartEvent(destination = found))
            } else {
                println("Could not find $arguments")
            }
        }
    }

    private fun foundMatch(args: List<String>, found: LocationNode): Boolean {
        if (found == LocationManager.NOWHERE_NODE) {
            return false
        }
        args.forEach {
            if (found.name.toLowerCase().contains(it)) {
                return true
            }
        }
        return false
    }

}