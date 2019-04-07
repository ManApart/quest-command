package combat

import combat.battle.position.TargetDistance
import core.commands.Command
import core.gameState.GameState
import core.history.display
import system.EventManager

class RetreatCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Retreat", "Backward", "Back")
    }

    override fun getDescription(): String {
        return "Retreat:\n\tMove further from the enemy."
    }

    override fun getManual(): String {
        return "\n\tRetreat - Move further from the enemy (only works in battle)."
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, args: List<String>) {
        when {
            GameState.battle == null -> display("This is only relevant in battle.")
            GameState.battle?.targetDistance?.distance == TargetDistance.getMaxDistance() -> display("You can't get any further.")
            else -> EventManager.postEvent(StartApproachEvent(GameState.player.creature, false))
        }
    }

}