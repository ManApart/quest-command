package explore.map

import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.history.display
import system.EventManager
import system.location.LocationManager

class ReadMapCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Map", "m")
    }

    override fun getDescription(): String {
        return "Map:\n\tGet information on your current and other locations"
    }

    override fun getManual(): String {
        return "\n\tMap *<location> - List your current location (or given location) and the surrounding areas." +
                "\n\tMap *depth - list neighbors to <depth> levels away from the location."
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args)
        val depth = arguments.getNumber() ?: 1
        val otherArgs = args.minus(depth.toString())

        when{
            otherArgs.isEmpty() -> currentLocation(depth)
            else -> targetLocation(otherArgs, depth)
        }
    }

    private fun currentLocation(depth: Int){
        EventManager.postEvent(ReadMapEvent(GameState.player.creature.location, depth))
    }

    private fun targetLocation(args: List<String>, depth: Int){
        val target = LocationManager.findLocation(args.joinToString(" "))
        if (target != LocationManager.NOWHERE_NODE){
            EventManager.postEvent(ReadMapEvent(target, depth))
        } else {
            println("Could not find ${args.joinToString(" ")} on the map.")
        }
    }

}