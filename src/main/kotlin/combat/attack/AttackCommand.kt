package combat.attack

import combat.HandHelper
import combat.battle.position.TargetAim
import core.commands.*
import core.gameState.GameState
import core.gameState.Target
import core.gameState.stat.HEALTH
import core.history.display
import interact.UseEvent
import interact.scope.ScopeManager
import system.EventManager

class AttackCommand : Command() {
    override fun getAliases(): Array<String> {
        val aliases = mutableListOf("Attack")
        AttackType.values().forEach {
            aliases.add(it.name.toLowerCase())
            aliases.add(it.alias.toLowerCase())
        }
        return aliases.toTypedArray()
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
        execute(GameState.player, keyword, args)
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        if (keyword.toLowerCase() == "attack") {
            clarifyAttackType(args)
        } else {
            val arguments = Args(args, listOf("with"))
            val attackType = fromString(keyword)
            val handHelper = HandHelper(source, arguments.getString("with"), attackType.damageType.damage.toLowerCase())
            val weaponName = handHelper.hand.getEquippedWeapon()?.name ?: handHelper.hand.name
            val target = getTarget(keyword, arguments, weaponName)

            if (target != null) {
                //Go ahead and process a target that has aimed for body parts or no body parts at all
                if (target.bodyPartTargets.isNotEmpty() || target.target.body.getRootPart() == null) {
                    processAttack(source, arguments, attackType, handHelper, target)
                } else {
                    //If we got an alias, process with a default value of the body root part
                    if (isAlias(keyword)) {
                        processAttack(source, arguments, attackType, handHelper, TargetAim(target.target, listOf(target.target.body.getRootPart()!!)))
                        //Otherwise clarify body parts.
                    } else {
                        clarifyTargetPart(keyword, target, weaponName)
                    }
                }
            }
        }
    }

    private fun getTarget(keyword: String, arguments: Args, weaponName: String): TargetAim? {
        val targets = parseTargets(arguments.getBaseGroup()) +  parseTargetsFromInventory(arguments.getBaseGroup())
        return if (targets.isEmpty() && !isAlias(keyword)) {
            clarifyTarget(keyword, weaponName)
            null
        } else if (targets.isEmpty() && GameState.battle != null) {
            val target = GameState.battle!!.getPlayerCombatant().lastAttacked
            return if (target != null) {
                TargetAim(target)
            } else {
                null
            }
        } else if (targets.size > 1) {
            clarifyTargets(keyword, targets, weaponName)
            null
        } else if (targets.size == 1) {
            targets.first()
        } else {
            clarifyTarget(keyword, weaponName)
            null
        }
    }

    private fun isAlias(keyword: String): Boolean {
        return AttackType.values().map { it.alias }.contains(keyword.toLowerCase())
    }

    private fun clarifyAttackType(args: List<String>) {
        val options = listOf("Chop", "Crush", "Slash", "Stab")
        val message = "Attack how?\n\t${options.joinToString(", ")}"
        val response = ResponseRequest(message, options.map { it to "$it ${args.joinToString(" ")}" }.toMap())
         CommandParser.setResponseRequest(response)
    }

    private fun clarifyTarget(keyword: String, weaponName: String) {
        val options = ScopeManager.getScope().getTargets()
        val message = "$keyword what with $weaponName?\n\t${options.joinToString(", ")}"
        val response = ResponseRequest(message, options.map { it.name to "$keyword ${it.name}" }.toMap())
         CommandParser.setResponseRequest(response)
    }

    private fun clarifyTargets(keyword: String, options: List<TargetAim>, weaponName: String) {
        val message = "$keyword which one with $weaponName?\n\t${options.joinToString(", ")}"
        val response = ResponseRequest(message, options.map { it.target.name to "$keyword ${it.target.name}" }.toMap())
         CommandParser.setResponseRequest(response)
    }

    private fun clarifyTargetPart(keyword: String, target: TargetAim, weaponName: String) {
        val options = target.target.body.getParts()
        val message = "$keyword what part of ${target.target.name} with $weaponName?\n\t${options.joinToString(", ") { it.name }}"
        val response = ResponseRequest(message, options.map { it.name to "$keyword ${it.name} of ${target.target.name}" }.toMap())
         CommandParser.setResponseRequest(response)
    }

    private fun processAttack(source: Target, arguments: Args, attackType: AttackType, handHelper: HandHelper, target: TargetAim?) {
        when {
            isAttackingActivatorWithWeapon(target, handHelper) -> EventManager.postEvent(UseEvent(source, handHelper.weapon!!, target!!.target))
            target != null && target.target == source && handHelper.weapon != null -> EventManager.postEvent(UseEvent(source, handHelper.weapon!!, source))
            target != null && !target.target.soul.hasStat(HEALTH) && handHelper.weapon != null -> EventManager.postEvent(UseEvent(source, handHelper.weapon!!, target.target))
            target != null -> EventManager.postEvent(StartAttackEvent(source, handHelper.hand, target, attackType.damageType))
            GameState.battle?.getCombatant(source)?.lastAttacked != null -> EventManager.postEvent(StartAttackEvent(source, handHelper.hand, TargetAim(GameState.battle!!.getCombatant(source)!!.lastAttacked!!), attackType.damageType))
            else -> display("Couldn't find ${arguments.getBaseString()}.")
        }
    }

    private fun isAttackingActivatorWithWeapon(target: TargetAim?, handHelper: HandHelper) =
            target != null && handHelper.weapon != null && target.target.properties.isActivator()


}