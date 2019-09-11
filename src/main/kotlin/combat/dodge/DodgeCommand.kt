package combat.dodge

import core.commands.Command
import core.commands.parseDirection
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
        return "\n\tDodge <direction> *<distance> - Attempt to dodge a blow (only works in battle)."
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (GameState.battle == null) {
            display("This is only relevant in battle.")
        } else {
            val direction = parseDirection(args).vector * 10
            EventManager.postEvent(StartDodgeEvent(GameState.player, direction))
        }
    }


}