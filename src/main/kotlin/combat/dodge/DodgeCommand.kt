package combat.dodge

import combat.battle.position.TargetDirection
import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.history.display
import system.EventManager

class DodgeCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Dodge")
    }

    override fun getDescription(): String {
        return "Dodge:\n\tAttempt to dodge a blow."
    }

    override fun getManual(): String {
        return "\n\tDodge <direction> - Attempt to dodge a blow (only works in battle)." +
                "\n\tYou return to a neutral position next time you choose an action."
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (GameState.battle == null) {
            display("This is only relevant in battle.")
        } else {
            val direction = getDirection(Args(args))
            EventManager.postEvent(StartDodgeEvent(GameState.player, direction))
        }
    }

    private fun getDirection(args: Args): TargetDirection {
        return TargetDirection.getTargetDirection(args.getGroupString(0)) ?: TargetDirection.getRandom()
    }

}