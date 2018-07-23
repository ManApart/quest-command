package combat.slash

import combat.HandHelper
import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import interact.ScopeManager
import system.EventManager

class SlashCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Slash")
    }

    override fun getDescription(): String {
        return "Slash:\n\tSlash the target"
    }

    override fun getManual(): String {
        return "\n\tSlash <target> - Slash the target with the item in your right hand" +
                "\n\tSlash <target> with <hand> - Slash the target with the item in your left/right hand" +
                "\n\tSlash <target> with <item> - Slash the target with the item in your left/right hand" +
                "\n\tSlashing a target damages it based on the slash damage of the item you're holding in that hand, or the slap damage you do if empty handed"
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, arguments: List<String>) {
        val args = Args(arguments, listOf("with"))
        val handHelper = HandHelper(args.getGroupString(1), "slashDamage")
        val cleaned = Args(arguments, listOf("with"), listOf("left", "l", "right", "r"))
        when {
            cleaned.argGroups.isEmpty() -> println("Slash what with your ${handHelper.hand.equippedName()}?")
            ScopeManager.targetExists(cleaned.argStrings[0]) -> EventManager.postEvent(SlashEvent(GameState.player.creature, handHelper.hand, ScopeManager.getTarget(cleaned.argStrings[0])))
            else -> println("Couldn't find ${cleaned.argStrings[0]}")
        }


    }
}