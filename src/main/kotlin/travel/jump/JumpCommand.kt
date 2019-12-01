package travel.jump

import core.commands.Command
import core.gameState.Direction
import core.gameState.GameState
import core.history.display
import system.EventManager

//TODO - eventually jump to specific part while climbing (in any direction)
class JumpCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Jump", "j")
    }

    override fun getDescription(): String {
        return "Jump:\n\tJump over obstacles or down to a lower area."

    }

    override fun getManual(): String {
        return "\n\tJump <obstacle> - Jump over an obstacle. X" +
                "\n\tJump - Jump down to the location below, possibly taking damage."
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (GameState.player.isClimbing) {
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