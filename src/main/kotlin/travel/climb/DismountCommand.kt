package travel.climb

import core.commands.Command
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.gameState.location.LocationPoint
import core.history.display
import system.EventManager

class DismountCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Dismount", "dis")
    }

    override fun getDescription(): String {
        return "Dismount:\n\tStop climbing (only at top or bottom of obstacle)"
    }

    override fun getManual(): String {
        return "\n\tDismount -Stop climbing (only at top or bottom of obstacle)"
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (GameState.player.isClimbing) {
            //If current location has a network connection/ exit, dismount there, otherwise dismount to target location if height 0
            val exit = getExitLocation()
            val climbTarget = GameState.player.climbTarget!!
            val targetLocation = climbTarget.location
            val part = GameState.player.location
            val origin = LocationPoint(targetLocation, climbTarget.name, part.name)

            when {
                exit != null -> EventManager.postEvent(ClimbCompleteEvent(GameState.player, GameState.player.climbTarget!!, origin, exit))
                GameState.player.location.getDistanceToLowestNodeInNetwork() == 0 -> EventManager.postEvent(ClimbCompleteEvent(GameState.player, GameState.player.climbTarget!!, origin, targetLocation))
                else -> display("You can't safely dismount from here, but you may be able to jump down.")
            }
        } else {
            display("You're not climbing.")
        }
    }

    private fun getExitLocation() : LocationNode? {
        val climbTarget = GameState.player.climbTarget!!
        val location = climbTarget.location
        val part = climbTarget.body.getPart(GameState.player.location.name)

        return climbTarget.location.getNeighborConnections().firstOrNull {
            it.source.equals(location, climbTarget, part)
        }?.destination?.location
    }


}