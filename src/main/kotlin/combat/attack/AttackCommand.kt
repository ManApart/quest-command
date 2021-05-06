package combat.attack

import combat.HandHelper
import core.GameState
import core.commands.*
import core.events.EventManager
import core.history.display
import core.target.Target
import status.stat.HEALTH
import traveling.position.TargetAim
import use.StartUseEvent

class AttackCommand : Command() {
    override fun getAliases(): List<String> {
        val aliases = mutableListOf("Attack")
        AttackType.values().forEach {
            aliases.add(it.name.lowercase())
            aliases.add(it.alias.lowercase())
        }
        return aliases
    }

    override val name = "Chop, Slash, Stab"

    override fun getDescription(): String {
        return "Chop/Stab/Slash/Crush the target"
    }

    override fun getManual(): String {
        return """
	<attack> <target> - Chop, crush, slash, or stab the target with the item in your right hand
	<attack> <target> with <hand> - Attack the target with the item in your left/right hand
	<attack> <part> of <target> - Attack the target, aiming for a specific body part
	<attack> <target> with <item> - Attack the target with the item in your left/right hand
	Attacking a target damages it based on the chop/stab/slash damage of the item you're holding in that hand, or the damage you do if empty handed"""
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        if (keyword.lowercase() == "attack") {
            clarifyAttackType(args)
        } else {
            val arguments = Args(args, listOf("with"))
            val attackType = fromString(keyword)
            val handHelper = HandHelper(source, arguments.getString("with"), attackType.damageType.damage.lowercase())
            val weaponName = handHelper.hand.getEquippedWeapon()?.name ?: handHelper.hand.name
            val target = getTarget(keyword, arguments, weaponName, source)

            if (target == null) {
                clarifyTarget(keyword, weaponName)
            } else {
                //Go ahead and process a target that has aimed for body parts or no body parts at all
                if (target.bodyPartTargets.isNotEmpty()) {
                    processAttack(source, arguments, attackType, handHelper, target)
                } else {
                    //If we got an alias, process with a default value of the body root part
                    if (isAlias(keyword) || target.target.body.getParts().size == 1) {
                        processAttack(source, arguments, attackType, handHelper, TargetAim(target.target, listOf(target.target.body.getRootPart())))
                        //Otherwise clarify body parts.
                    } else {
                        clarifyTargetPart(keyword, target, weaponName)
                    }
                }
            }
        }
    }

    private fun getTarget(keyword: String, arguments: Args, weaponName: String, source: Target): TargetAim? {
        val targets = parseTargets(arguments.getBaseGroup()) + parseTargetsFromInventory(arguments.getBaseGroup())
        return if (targets.isEmpty() && !isAlias(keyword)) {
            clarifyTarget(keyword, weaponName)
            null
        } else if (targets.isEmpty()) {
            val target = source.ai.aggroTarget
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
        return AttackType.values().map { it.alias }.contains(keyword.lowercase())
    }

    private fun clarifyAttackType(args: List<String>) {
        val options = listOf("Chop", "Crush", "Slash", "Stab")
        val message = "Attack how?\n\t${options.joinToString(", ")}"
        val response = ResponseRequest(message, options.associateWith { "$it ${args.joinToString(" ")}" })
        CommandParser.setResponseRequest(response)
    }

    private fun clarifyTarget(keyword: String, weaponName: String) {
        val options = GameState.currentLocation().getTargets()
        val message = "$keyword what with $weaponName?\n\t${options.joinToString(", ")}"
        val response = ResponseRequest(message, options.associate { it.name to "$keyword ${it.name}" })
        CommandParser.setResponseRequest(response)
    }

    private fun clarifyTargets(keyword: String, options: List<TargetAim>, weaponName: String) {
        val message = "$keyword which one with $weaponName?\n\t${options.joinToString(", ")}"
        val response = ResponseRequest(message, options.associate { it.target.name to "$keyword ${it.target.name}" })
        CommandParser.setResponseRequest(response)
    }

    private fun clarifyTargetPart(keyword: String, target: TargetAim, weaponName: String) {
        val options = target.target.body.getParts()
        val message = "$keyword what part of ${target.target.name} with $weaponName?\n\t${options.joinToString(", ") { it.name }}"
        val response = ResponseRequest(message,
            options.associate { it.name to "$keyword ${it.name} of ${target.target.name}" })
        CommandParser.setResponseRequest(response)
    }

    private fun processAttack(source: Target, arguments: Args, attackType: AttackType, handHelper: HandHelper, target: TargetAim?) {
        when {
            isAttackingActivatorWithWeapon(target, handHelper) -> EventManager.postEvent(StartUseEvent(source, handHelper.weapon!!, target!!.target))
            target != null && target.target == source && handHelper.weapon != null -> EventManager.postEvent(StartUseEvent(source, handHelper.weapon!!, source))
            target != null && !target.target.soul.hasStat(HEALTH) && handHelper.weapon != null -> EventManager.postEvent(StartUseEvent(source, handHelper.weapon!!, target.target))
            target != null -> EventManager.postEvent(StartAttackEvent(source, handHelper.hand, target, attackType.damageType))
            source.ai.aggroTarget != null -> EventManager.postEvent(StartAttackEvent(source, handHelper.hand, TargetAim(source.ai.aggroTarget!!), attackType.damageType))
            else -> display("Couldn't find ${arguments.getBaseString()}.")
        }
    }

    private fun isAttackingActivatorWithWeapon(target: TargetAim?, handHelper: HandHelper) =
        target != null && handHelper.weapon != null && target.target.properties.isActivator()


}