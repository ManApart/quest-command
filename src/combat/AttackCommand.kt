package combat

import combat.chop.ChopEvent
import combat.slash.SlashEvent
import combat.stab.StabEvent
import core.commands.Args
import core.commands.Command
import core.events.Event
import core.gameState.BodyPart
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Target
import interact.ScopeManager
import system.EventManager

class AttackCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Chop", "Slash", "Stab")
    }

    override fun getDescription(): String {
        return "Attack:\n\tChop/Stab/Slash the target"
    }

    override fun getManual(): String {
        return "\n\t<attack> <target> - Chop, slash, or stab the target with the item in your right hand" +
                "\n\t<attack> <target> with <hand> - Attack the target with the item in your left/right hand" +
                "\n\t<attack> <target> with <item> - Attack the target with the item in your left/right hand" +
                "\n\tAttacking a target damages it based on the chop/stab/slash damage of the item you're holding in that hand, or the damage you do if empty handed"
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, arguments: List<String>) {
        val args = Args(arguments, listOf("with"))
        val damageType = getDamageType(keyword)
        val handHelper = HandHelper(args.getGroupString(1), damageType)
        val cleaned = Args(arguments, listOf("with"), listOf("left", "l", "right", "r"))
        when {
            cleaned.argGroups.isEmpty() -> println("${keyword.capitalize()} what with your ${handHelper.hand.equippedName()}?")
            ScopeManager.targetExists(cleaned.argStrings[0]) -> EventManager.postEvent(createEvent(keyword, handHelper.hand, ScopeManager.getTarget(cleaned.argStrings[0])))
            else -> println("Couldn't find ${cleaned.argStrings[0]}")
        }
    }

    private fun getDamageType(keyword: String) : String {
        return when (keyword) {
            "chop" -> "chopDamage"
            "slash" -> "slashDamage"
            "stab" -> "stabDamage"
            else -> "unknown"
        }
    }
    private fun createEvent(keyword: String, sourcePart: BodyPart, target: Target) : Event {
        return when (keyword) {
            "chop" -> ChopEvent(GameState.player.creature, sourcePart, target)
            "slash" -> SlashEvent(GameState.player.creature, sourcePart, target)
            else -> StabEvent(GameState.player.creature, sourcePart, target)
        }
    }
}