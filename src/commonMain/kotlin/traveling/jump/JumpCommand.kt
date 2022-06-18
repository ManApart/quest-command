package traveling.jump

import core.Player
import core.commands.Command
import core.events.EventManager
import core.history.displayToMe
import core.properties.IS_CLIMBING
import core.thing.Thing
import traveling.direction.Direction

//TODO - eventually jump to specific part while climbing (in any direction)
class JumpCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Jump", "j")
    }

    override fun getDescription(): String {
        return "Jump over obstacles or down to a lower area."
    }

    override fun getManual(): String {
        return """
	Jump <obstacle> - Jump over an obstacle. X
	Jump - Jump down to the location below, possibly taking damage."""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> source.thing.currentLocation().getActivators(perceivedBy = source.thing).map { it.name }
            else -> listOf()
        }
    }

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        if (source.properties.values.getBoolean(IS_CLIMBING)) {
            val playerLocation = source.location
            val thingLocation= source.climbThing!!.location
            EventManager.postEvent(JumpEvent(source, source = playerLocation, destination = thingLocation, fallDistance = playerLocation.getDistanceToLowestNodeInNetwork()))
        } else {
            val found = source.location.getNeighbors(Direction.BELOW).firstOrNull()
            if (found != null) {
                EventManager.postEvent(JumpEvent(source, source = source.location, destination = found))
            } else {
                source.displayToMe("Couldn't find anything below to jump down to.")
            }
        }
    }

}