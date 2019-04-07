package travel

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.gameState.location.NOWHERE_NODE
import core.history.display
import system.EventManager
import system.location.LocationManager

class TravelCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Travel", "move", "t", "go", "mv")
    }

    override fun getDescription(): String {
        return "Travel:\n\tTravel to different locations."
    }

    override fun getManual(): String {
        return "\n\tTravel to <location> - Start traveling to a location, if a route can be found." +
                "\n\tTravel - Continue traveling to a goal location." +
                "\n\tTo view a route, see the Route command"
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (args.isEmpty()) {
            val route = GameState.player.route
            val source = GameState.player.creature.location
            when {
                route == null -> display("No route to travel to.")
                route.destination == source -> display("You're already at the end of the route.")
                route.isOnRoute(source) -> EventManager.postEvent(TravelStartEvent(destination = route.getNextStep(source).destination))
                else -> display("You're not on a route right now.")
            }
        } else if (CommandParser.getCommand<TravelInDirectionCommand>().getAliases().map { it.toLowerCase() }.contains(args[0].toLowerCase())) {
            CommandParser.parseCommand(args.joinToString(" "))
        } else {
            val arguments = Args(args, excludedWords = listOf("to"))
            val found = LocationManager.findLocation(arguments.argGroups[0].joinToString(" "))

            if (foundMatch(arguments.argGroups[0], found)) {
                EventManager.postEvent(FindRouteEvent(GameState.player.creature.location, found, 4, true))
            } else {
                display("Could not find $arguments")
            }
        }
    }

    private fun foundMatch(args: List<String>, found: LocationNode): Boolean {
        if (found == NOWHERE_NODE) {
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