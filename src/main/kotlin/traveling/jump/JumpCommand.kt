package traveling.jump

import core.GameState
import core.commands.Command
import core.events.EventManager
import core.history.display
import core.properties.IS_CLIMBING
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

    override fun execute(keyword: String, args: List<String>) {
        if (GameState.player.properties.values.getBoolean(IS_CLIMBING)) {
            val playerLocation = GameState.player.location
            val targetLocation= GameState.player.climbTarget!!.location
            EventManager.postEvent(JumpEvent(source = playerLocation, destination = targetLocation, fallDistance = playerLocation.getDistanceToLowestNodeInNetwork()))
        } else {
            val found = GameState.player.location.getNeighbors(Direction.BELOW).firstOrNull()
            if (found != null) {
                EventManager.postEvent(JumpEvent(source = GameState.player.location, destination = found))
            } else {
                display("Couldn't find anything below to jump down to.")
            }
        }
    }

}