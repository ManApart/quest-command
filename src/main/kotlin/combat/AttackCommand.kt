package combat

import combat.chop.ChopEvent
import combat.crush.CrushEvent
import combat.slash.SlashEvent
import combat.stab.StabEvent
import core.commands.Args
import core.commands.Command
import core.events.Event
import core.gameState.BodyPart
import core.gameState.GameState
import core.gameState.Target
import interact.ScopeManager
import system.EventManager

class AttackCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Chop", "Slash", "Stab")
    }

    override fun getDescription(): String {
        return "Attack:\n\tChop/Stab/Slash/Crush the target"
    }

    override fun getManual(): String {
        return "\n\t<attack> <target> - Chop, crush, slash, or stab the target with the item in your right hand" +
                "\n\t<attack> <target> with <hand> - Attack the target with the item in your left/right hand" +
                "\n\t<attack> <direction> of <target> - Attack the target, aiming in a direction (${TargetDirection.getPrimaryAliases().joinToString(", ")})" +
                "\n\t<attack> <direction> - Attack the target you are battling, aiming in a direction (${TargetDirection.getPrimaryAliases().joinToString(", ")}) X" +
                "\n\t<attack> <target> with <item> - Attack the target with the item in your left/right hand" +
                "\n\tAttacking a target damages it based on the chop/stab/slash damage of the item you're holding in that hand, or the damage you do if empty handed"
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, arguments: List<String>) {
        val args = Args(arguments, listOf("with", "of"))
        val damageType = getDamageType(keyword)
        val direction = getDirection(args)
        val handHelper = HandHelper(args.getGroupString(1), damageType)

        val ignoredWords = mutableListOf("left", "l", "right", "r", "of")
        ignoredWords.addAll(TargetDirection.getAllAliases())
        val cleaned = Args(arguments, listOf("with"), ignoredWords)
        when {
            cleaned.argGroups.isEmpty() -> println("${keyword.capitalize()} what with your ${handHelper.hand.equippedName()}?")
            ScopeManager.targetExists(cleaned.argStrings[0]) -> EventManager.postEvent(createEvent(keyword, handHelper.hand, ScopeManager.getTarget(cleaned.argStrings[0]), direction))
            GameState.battle != null -> EventManager.postEvent(createEvent(keyword, handHelper.hand, GameState.battle!!.playerLastAttacked, direction))
            else -> println("Couldn't find ${cleaned.argStrings[0]}")
        }
    }

    private fun getDamageType(keyword: String) : String {
        return when (keyword) {
            "chop" -> "chopDamage"
            "crush" -> "crushDamage"
            "slash" -> "slashDamage"
            "stab" -> "stabDamage"
            else -> "unknown"
        }
    }

    private fun getDirection(args: Args): TargetDirection {
        return TargetDirection.getTargetDirection(args.getGroupString(0)) ?: TargetDirection.getRandom()
    }

    private fun createEvent(keyword: String, sourcePart: BodyPart, target: Target, direction: TargetDirection) : Event {
        return when (keyword) {
            "chop" -> ChopEvent(GameState.player.creature, sourcePart, target, direction)
            "crush" -> CrushEvent(GameState.player.creature, sourcePart, target, direction)
            "slash" -> SlashEvent(GameState.player.creature, sourcePart, target, direction)
            else -> StabEvent(GameState.player.creature, sourcePart, target, direction)
        }
    }
}