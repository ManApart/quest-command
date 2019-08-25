package combat.attack

import combat.DamageType
import combat.HandHelper
import combat.battle.position.TargetAim
import core.commands.Args
import core.commands.Command
import core.commands.parseTargets
import core.gameState.GameState
import core.gameState.body.BodyPart
import core.history.display
import interact.UseEvent
import interact.scope.ScopeManager
import system.EventManager

//TODO - give choice if more than one target found
//TODO - give choice if just 'attack' instead of a type of attack
//TODO -response request for what part to attack
class AttackCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Chop", "Slash", "Stab")
    }

    override val name = "Chop, Slash, Stab"

    override fun getDescription(): String {
        return "Attack:\n\tChop/Stab/Slash/Crush the target"
    }

    override fun getManual(): String {
        return "\n\t<attack> <target> - Chop, crush, slash, or stab the target with the item in your right hand" +
                "\n\t<attack> <target> with <hand> - Attack the target with the item in your left/right hand" +
                "\n\t<attack> <part> of <target> - Attack the target, aiming for a specific body part" +
                "\n\t<attack> <target> with <item> - Attack the target with the item in your left/right hand" +
                "\n\tAttacking a target damages it based on the chop/stab/slash damage of the item you're holding in that hand, or the damage you do if empty handed"
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, listOf("with"))
        val damageType = getDamageType(keyword)
        val handHelper = HandHelper(arguments.getGroupString(1), damageType)

        val scope = ScopeManager.getScope()
        var target = parseTargets("", arguments.getGroup(0)).firstOrNull()

        //TODO - use response request
        if (target?.bodyPartTargets?.isEmpty() == true && target.target.body.layout.rootNode?.getLocation()?.bodyPart != null){
            target = TargetAim(target.target, listOf(target.target.body.layout.rootNode?.getLocation()?.bodyPart!!))
        }

        when {
            arguments.argGroups.isEmpty() -> display("${keyword.capitalize()} what with your ${handHelper.hand.getEquippedWeapon()}?")
            isAttackingActivatorWithWeapon(arguments, handHelper) -> EventManager.postEvent(UseEvent(GameState.player, handHelper.weapon!!, scope.getTargets(arguments.argStrings[0]).first()))
            target != null -> EventManager.postEvent(createEvent(keyword, handHelper.hand, target))
            GameState.player.inventory.getItem(arguments.argStrings[0]) != null -> EventManager.postEvent(createEvent(keyword, handHelper.hand, TargetAim(GameState.player.inventory.getItem(arguments.argStrings[0])!!)))
            GameState.battle != null -> EventManager.postEvent(createEvent(keyword, handHelper.hand, TargetAim(GameState.battle!!.playerLastAttacked.target)))
            else -> display("Couldn't find ${arguments.argStrings[0]}.")
        }
    }

    private fun getDamageType(keyword: String): String {
        return when (keyword) {
            "chop" -> "chopDamage"
            "crush" -> "crushDamage"
            "slash" -> "slashDamage"
            "stab" -> "stabDamage"
            else -> "unknown"
        }
    }

    private fun isAttackingActivatorWithWeapon(cleaned: Args, handHelper: HandHelper) =
            ScopeManager.getScope().getActivators(cleaned.argStrings[0]).isNotEmpty() && handHelper.weapon != null


    private fun createEvent(keyword: String, sourcePart: BodyPart, target: TargetAim): StartAttackEvent {
        return when (keyword) {
            "chop" -> StartAttackEvent(GameState.player, sourcePart, target, DamageType.CHOP)
            "crush" -> StartAttackEvent(GameState.player, sourcePart, target, DamageType.CRUSH)
            "slash" -> StartAttackEvent(GameState.player, sourcePart, target, DamageType.SLASH)
            else -> StartAttackEvent(GameState.player, sourcePart, target, DamageType.STAB)
        }
    }
}