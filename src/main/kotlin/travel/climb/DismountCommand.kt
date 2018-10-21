package travel.climb

import core.commands.Command
import core.gameState.GameState
import interact.ScopeManager
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
        return listOf("Travel")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (isClimbing()) {
            val journey = GameState.journey as ClimbJourney
            when {
                journey.getCurrentSegment().top -> EventManager.postEvent(ClimbCompleteEvent(GameState.player.creature, journey.target, GameState.player.creature.location, journey.top))
                journey.getCurrentSegment().bottom -> EventManager.postEvent(ClimbCompleteEvent(GameState.player.creature, journey.target, GameState.player.creature.location, journey.bottom))
                else -> println("You can't safely dismount from here, but you may be able to jump down.")
            }
        } else {
            println("You're not climbing.")
        }
    }

    private fun isClimbing(): Boolean {
        return GameState.journey != null && GameState.journey is ClimbJourney
    }

}