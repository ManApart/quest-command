package combat

import combat.battle.position.TargetDirection
import combat.chop.ChopEvent
import combat.crush.CrushEvent
import combat.slash.SlashEvent
import combat.stab.StabEvent
import core.commands.Args
import core.commands.Command
import core.events.Event
import core.gameState.Activator
import core.gameState.body.BodyPart
import core.gameState.GameState
import core.gameState.Target
import core.history.display
import interact.scope.ScopeManager
import interact.UseEvent
import system.EventManager

//TODO - give choice if more than one target found
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

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, listOf("with", "of"))
        val damageType = getDamageType(keyword)
        val direction = getDirection(arguments)
        val handHelper = HandHelper(arguments.getGroupString(1), damageType)

        val ignoredWords = mutableListOf<String>()
        ignoredWords.addAll(TargetDirection.getAllAliases())
        val cleaned = Args(args, listOf("with", "of"), ignoredWords)
        val scope = ScopeManager.getScope()
        when {
            cleaned.argGroups.isEmpty() -> display("${keyword.capitalize()} what with your ${handHelper.hand.getEquippedWeapon()}?")
            isAttackingActivatorWithWeapon(cleaned, handHelper) -> EventManager.postEvent(UseEvent(GameState.player.creature, handHelper.weapon!!, scope.getTargets(cleaned.argStrings[0]).first()))
            scope.getTargets(cleaned.argStrings[0]).isNotEmpty() -> EventManager.postEvent(createEvent(keyword, handHelper.hand, scope.getTargets(cleaned.argStrings[0]).first(), direction))
            GameState.player.creature.inventory.getItem(cleaned.argStrings[0]) != null -> EventManager.postEvent(createEvent(keyword, handHelper.hand, GameState.player.creature.inventory.getItem(cleaned.argStrings[0])!!, direction))
            GameState.battle != null -> EventManager.postEvent(createEvent(keyword, handHelper.hand, GameState.battle!!.playerLastAttacked, direction))
            else -> display("Couldn't find ${cleaned.argStrings[0]}")
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

    private fun isAttackingActivatorWithWeapon(cleaned: Args, handHelper: HandHelper) =
            ScopeManager.getScope().getTargets(cleaned.argStrings[0]).isNotEmpty() && ScopeManager.getScope().getTargets(cleaned.argStrings[0]).first() is Activator && handHelper.weapon != null

    private fun getDirection(args: Args): TargetDirection {
        return TargetDirection.getTargetDirection(args.getGroupString(0)) ?: TargetDirection.getRandom()
    }

    private fun createEvent(keyword: String, sourcePart: BodyPart, target: Target, direction: TargetDirection) : Event {
        return when (keyword) {
            "chop" -> ChopEvent(GameState.player.creature, sourcePart, target, direction.position)
            "crush" -> CrushEvent(GameState.player.creature, sourcePart, target, direction.position)
            "slash" -> SlashEvent(GameState.player.creature, sourcePart, target, direction.position)
            else -> StabEvent(GameState.player.creature, sourcePart, target, direction.position)
        }
    }
}