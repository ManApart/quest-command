package traveling.climb

import core.GameState
import core.commands.Command
import core.events.EventManager
import core.history.display
import core.properties.IS_CLIMBING
import traveling.location.location.LocationPoint

class DismountCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Dismount", "dis")
    }

    override fun getDescription(): String {
        return "Stop climbing (only at top or bottom of obstacle)"
    }

    override fun getManual(): String {
        return """
	Dismount -Stop climbing (only at top or bottom of obstacle)"""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (GameState.player.properties.values.getBoolean(IS_CLIMBING)) {
            //If current location has a network connection/ exit, dismount there, otherwise dismount to target location if height 0
            val exit = getExitLocation()
            val climbTarget = GameState.player.climbTarget!!
            val targetLocation = LocationPoint(climbTarget.location)
            val part = GameState.player.location
            val origin = LocationPoint(climbTarget.location, climbTarget.name, part.name)

            when {
                exit != null -> EventManager.postEvent(ClimbCompleteEvent(GameState.player, GameState.player.climbTarget!!, origin, exit))
                GameState.player.location.getDistanceToLowestNodeInNetwork() == 0 -> EventManager.postEvent(ClimbCompleteEvent(GameState.player, GameState.player.climbTarget!!, origin, targetLocation))
                else -> display("You can't safely dismount from here, but you may be able to jump down.")
            }
        } else {
            display("You're not climbing.")
        }
    }

    private fun getExitLocation() : LocationPoint? {
        val climbTarget = GameState.player.climbTarget!!
        val location = climbTarget.location
        val part = climbTarget.body.getPart(GameState.player.location.name)

        return climbTarget.location.getNeighborConnections().firstOrNull {
            it.source.equals(location, climbTarget, part)
        }?.destination
    }


}