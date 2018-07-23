package battle.stab

import battle.HandHelper
import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import interact.ScopeManager
import system.EventManager

class StabCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Stab")
    }

    override fun getDescription(): String {
        return "Stab:\n\tStab the target"
    }

    override fun getManual(): String {
        return "\n\tStab <target> - Stab the target with the item in your right hand" +
                "\n\tStab <target> with <hand> - Stab the target with the item in your left/right hand" +
                "\n\tStab <target> with <item> - Stab the target with the item in your left/right hand" +
                "\n\tStabbing a target damages it based on the stab damage of the item you're holding in that hand, or the punch damage you do if empty handed"
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, arguments: List<String>) {
        val args = Args(arguments, listOf("with"))
        val handHelper = HandHelper(args.getGroupString(1), "stabDamage")
        val cleaned = Args(arguments, listOf("with"), listOf("left", "l", "right", "r"))
        when {
            cleaned.argGroups.isEmpty() -> println("Stab what with your ${handHelper.hand.equippedName()}?")
            ScopeManager.targetExists(cleaned.argStrings[0]) -> EventManager.postEvent(StabEvent(GameState.player.creature, handHelper.hand, ScopeManager.getTarget(cleaned.argStrings[0])))
            else -> println("Couldn't find ${cleaned.argStrings[0]}")
        }


    }
}