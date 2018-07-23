package combat.chop

import combat.HandHelper
import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import interact.ScopeManager
import system.EventManager

class ChopCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Chop")
    }

    override fun getDescription(): String {
        return "Chop:\n\tChop the target"
    }

    override fun getManual(): String {
        return "\n\tChop <target> - Chop the target with the item in your right hand" +
                "\n\tChop <target> with <hand> - Chop the target with the item in your left/right hand" +
                "\n\tChop <target> with <item> - Chop the target with the item in your left/right hand" +
                "\n\tChoping a target damages it based on the chop damage of the item you're holding in that hand, or the chop damage you do if empty handed"
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, arguments: List<String>) {
        val args = Args(arguments, listOf("with"))
        val handHelper = HandHelper(args.getGroupString(1), "chopDamage")
        val cleaned = Args(arguments, listOf("with"), listOf("left", "l", "right", "r"))
        when {
            cleaned.argGroups.isEmpty() -> println("Chop what with your ${handHelper.hand.equippedName()}?")
            ScopeManager.targetExists(cleaned.argStrings[0]) -> EventManager.postEvent(ChopEvent(GameState.player.creature, handHelper.hand, ScopeManager.getTarget(cleaned.argStrings[0])))
            else -> println("Couldn't find ${cleaned.argStrings[0]}")
        }


    }
}