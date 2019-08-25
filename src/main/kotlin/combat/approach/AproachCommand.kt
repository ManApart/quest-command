package combat.approach

import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.history.display
import system.EventManager

class AproachCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Approach", "Forward", "Advance")
    }

    override fun getDescription(): String {
        return "Approach:\n\tMove closer to the enemy."
    }

    override fun getManual(): String {
        return "\n\tApproach - Move closer to the enemy (only works in battle)"
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, args: List<String>) {
        //TODO - request response
        //TODO - default to max movement allowed by agility
        val amount = Args(args).getNumber() ?: 2
        when {
            GameState.battle == null -> display("This is only relevant in battle.")
            GameState.battle?.getCombatantDistance() ?: 0 <= 1 -> display("You can't get any closer.")
            else -> EventManager.postEvent(StartApproachEvent(GameState.player, amount,true))
        }
    }

}