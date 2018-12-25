package travel

import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.history.display
import explore.map.ReadMapEvent
import system.EventManager
import system.location.LocationManager

class RouteCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Route", "r")
    }

    override fun getDescription(): String {
        return "Route:\n\tView your current Route."
    }

    override fun getManual(): String {
        return "\n\tRoute - View your current route." +
                "\n\tRoute *<location> - Find a route to <location>." +
                "\n\tRoutes are used with the Move command."
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args)
        val depth = arguments.getNumber() ?: 5
        val otherArgs = args.minus(depth.toString())

        when{
            otherArgs.isEmpty() -> EventManager.postEvent(ViewRouteEvent())
            else -> targetLocation(otherArgs, depth)
        }
    }

    private fun targetLocation(args: List<String>, depth: Int){
        val target = LocationManager.findLocation(args.joinToString(" "))
        if (target != LocationManager.NOWHERE_NODE){
            EventManager.postEvent(FindRouteEvent(GameState.player.creature.location, target, depth))
        } else {
            println("Could not find ${args.joinToString(" ")} on the map.")
        }
    }

}