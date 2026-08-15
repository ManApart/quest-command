package traveling.jump

import core.Player
import core.commands.Command
import core.commands.args
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.properties.IS_CLIMBING
import traveling.direction.Direction
import traveling.location.network.LocationNode

class JumpCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Jump", "j")
    }

    override fun getDescription(): String {
        return "Jump over obstacles or down to a lower area."
    }

    override fun getManual(): String {
        return """
	Jump - Jump down to the location below, possibly taking damage.
    Jump to <location> - Jump to a specific location below."""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> source.thing.currentLocation().getActivators(perceivedBy = source.thing).map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        if (source.properties.values.getBoolean(IS_CLIMBING) && source.thing.climbThing != null) {
            val playerLocation = source.location
            val climbThing = source.thing.climbThing!!
            EventManager.postEvent(JumpEvent(source.thing, source = playerLocation, destination = climbThing.location, fallDistance = climbThing.getHeight()))
        } else {
            val arguments = args(args, "to")
            val toName = arguments.getString("to").takeIf { it.isNotBlank() }
            val locations = source.location.getNeighbors(Direction.BELOW).hasToName(toName)
            when {
                locations.isEmpty() -> source.displayToMe("Couldn't find anything below to jump down to.")
                locations.size == 1 -> EventManager.postEvent(JumpEvent(source.thing, source = source.location, destination = locations.first()))
                else -> source.respond("There is nothing to jump down to.") {
                    message("Jump to where?")
                    options(locations.map { "jump to ${it.name}" })
                }
            }
        }
    }

    private fun List<LocationNode>.hasToName(toName: String?) = if (toName == null) this else filter {
        it.name.lowercase().contains(toName)
    }

}
