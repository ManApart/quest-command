package traveling.routes

import core.commands.Args
import core.commands.Command
import core.GameState
import traveling.location.location.NOWHERE_NODE
import core.events.EventManager
import traveling.location.location.LocationManager

class RouteCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Route", "rr")
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
        val target = LocationManager.getNetwork().findLocation(args.joinToString(" "))
        if (target != NOWHERE_NODE){
            EventManager.postEvent(FindRouteEvent(GameState.player.location, target, depth))
        } else {
            println("Could not find ${args.joinToString(" ")} on the map.")
        }
    }

}