package combat.dodge

import core.commands.Command
import core.commands.parseDirection
import core.GameState
import core.events.EventManager
import traveling.move.StartMoveEvent

class DodgeCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Dodge")
    }

    override fun getDescription(): String {
        return "Attempt to dodge a blow."
    }

    override fun getManual(): String {
        return """
	Dodge <direction> *<distance> - Attempt to dodge a blow. Uses more stamina, but is faster than just moving."""
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, args: List<String>) {
        val direction = parseDirection(args).vector * 10
        EventManager.postEvent(StartMoveEvent(GameState.player, direction, 2f, 1.5f))
    }


}