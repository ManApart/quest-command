package traveling.travel

import core.GameState
import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.events.EventManager
import core.history.display
import traveling.location.location.LocationManager
import traveling.location.location.LocationNode
import traveling.location.location.NOWHERE_NODE
import traveling.routes.FindRouteEvent

class TravelCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Travel", "t", "go", "cd")
    }

    override fun getDescription(): String {
        return "Travel to different locations."
    }

    override fun getManual(): String {
        return """
	Travel to <location> - Start traveling to a location, if a route can be found.
	Travel - Continue traveling to a goal location.
	Travel s - The s flag silences travel, meaning a minimum amount of output
	To view a route, see the Route command"""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (GameState.player.getEncumbrance() >=1){
            display("You are too encumbered to travel.")
        } else if (args.isEmpty()) {
            val route = GameState.player.route
            val source = GameState.player.location
            when {
                route == null -> display("No route to travel to.")
                route.destination == source -> display("You're already at the end of the route.")
                route.isOnRoute(source) -> EventManager.postEvent(TravelStartEvent(destination = route.getNextStep(source).destination.location))
                else -> display("You're not on a route right now.")
            }
        } else if (CommandParser.getCommand<TravelInDirectionCommand>().getAliases().map { it.lowercase() }.contains(args[0].lowercase())) {
            CommandParser.parseCommand(args.joinToString(" "))
        } else {
            val arguments = Args(args, excludedWords = listOf("to"), flags = listOf("s"))
            val foundName = arguments.getBaseString()

            if (LocationManager.networkExists()) {
                val found = LocationManager.getNetwork().findLocation(foundName)

                if (foundMatch(arguments.getBaseGroup(), found)) {
                    EventManager.postEvent(FindRouteEvent(GameState.player.location, found, 4, true, arguments.hasFlag("s")))
                } else {
                    display("Could not find $arguments")
                }
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
            if (found.name.lowercase().contains(it)) {
                return true
            }
        }
        return false
    }

}