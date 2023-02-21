package traveling.travel

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.CommandParsers
import core.events.EventManager
import core.history.displayToMe
import traveling.location.RouteNeighborFinder
import traveling.location.location.LocationManager
import traveling.location.network.LocationNode
import traveling.location.network.NOWHERE_NODE
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

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> listOf("to")
            args.last() == "to" -> source.location.getNeighbors().map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        if (source.thing.getEncumbrance() >=1){
            source.displayToMe("You are too encumbered to travel.")
        } else if (args.isEmpty()) {
            val route = source.thing.mind.route
            val sourceLocation = source.location
            when {
                route == null -> source.displayToMe("No route to travel to.")
                route.destination == sourceLocation -> source.displayToMe("You're already at the end of the route.")
                route.isOnRoute(sourceLocation) -> EventManager.postEvent(TravelStartEvent(source.thing, destination = route.getNextStep(sourceLocation).destination.location))
                else -> source.displayToMe("You're not on a route right now.")
            }
        } else if (CommandParsers.getCommand<TravelInDirectionCommand>().getAliases().map { it.lowercase() }.contains(args[0].lowercase())) {
            CommandParsers.parseCommand(source, args.joinToString(" "))
        } else {
            val arguments = Args(args, excludedWords = listOf("to"), flags = listOf("s"))
            val foundName = arguments.getBaseString()

            if (LocationManager.networkExists(source.location.parent)) {
                val found = LocationManager.getNetwork(source.location.parent).findLocation(foundName)

                if (foundMatch(arguments.getBaseGroup(), found)) {
                    EventManager.postEvent(FindRouteEvent(source.thing, source.location, found, 4, true, arguments.hasFlag("s")))
                } else {
                    source.displayToMe("Could not find $arguments")
                }
            } else {
                source.displayToMe("Could not find $arguments")
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