package travel.climb

import core.commands.Command
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.history.display
import system.EventManager

class DismountCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Dismount")
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
            val exit = getExitLocation(GameState.player.location)


            when {
                exit != null -> EventManager.postEvent(ClimbCompleteEvent(GameState.player, GameState.player.climbTarget!!, GameState.player.location, exit))
                GameState.player.location.getDistanceToLowestNodeInNetwork() == 0 -> EventManager.postEvent(ClimbCompleteEvent(GameState.player, GameState.player.climbTarget!!, GameState.player.location, GameState.player.location))
                else -> display("You can't safely dismount from here, but you may be able to jump down.")
            }
        } else {
            display("You're not climbing.")
        }
    }

    //TODO - could this be a bad test for if the network location exit is not climbing related?
    private fun getExitLocation(location: LocationNode) : LocationNode? {
        return location.getNeighborConnections().firstOrNull { it.isNetworkConnection() }?.destination?.location
    }


}