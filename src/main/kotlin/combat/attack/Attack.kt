package combat.attack

import combat.Combatant
import combat.DamageType
import combat.battle.Distances
import combat.battle.position.TargetAim
import combat.takeDamage.TakeDamageEvent
import core.events.EventListener
import core.gameState.GameState
import core.gameState.Target
import core.gameState.body.BodyPart
import core.gameState.stat.BARE_HANDED
import core.history.display
import core.utility.StringFormatter
import core.utility.max
import interact.UseEvent
import system.EventManager

class Attack : EventListener<AttackEvent>() {

    override fun execute(event: AttackEvent) {
        if (isValidAttack(event)) {
            val defender = GameState.battle!!.getCombatant(event.target.target)!!
            val offensiveDamage = getOffensiveDamage(event.source, event.sourcePart, event.type)
            val damageSource = event.sourcePart.getEquippedWeapon()?.name ?: event.sourcePart.name
            val targetDistance = GameState.battle?.getCombatantDistance() ?: Distances.MIN_RANGE
            val weaponRange = getRange(event.source, event.sourcePart)

            when {
                weaponRange < targetDistance -> display("${event.target} is too far away to be hit by $damageSource.")
                offensiveDamage > 0 -> processAttack(defender, event, damageSource, offensiveDamage)
                event.sourcePart.getEquippedWeapon() != null -> EventManager.postEvent(UseEvent(GameState.player, event.sourcePart.getEquippedWeapon()!!, event.target.target))
                else -> display("Nothing happens.")
            }
        }
        event.target.target.consume(event)
    }

    private fun isValidAttack(event: AttackEvent): Boolean {
        return when {
            GameState.battle == null -> {
                println("Attack has no battle context.")
                false
            }
            GameState.battle?.getCombatant(event.target.target) == null -> {
                println("Attack battle has no combatant for target.")
                false
            }
            else -> true
        }
    }

    private fun processAttack(defender: Combatant, event: AttackEvent, damageSource: String, offensiveDamage: Int) {
        val subject = StringFormatter.getSubject(event.source)
        val defenderName = StringFormatter.getSubject(event.target.target)
        val attackedParts = getAttackedParts(event.source, event.sourcePart, event.target)

        if (attackedParts.isEmpty()) {
            display("$subject ${StringFormatter.format(event.source.isPlayer(), "miss", "misses")}!")
        } else {
            val verb = StringFormatter.format(event.source.isPlayer(), event.type.verbPlural, event.type.verb)
//            display("$subject $verb at $defenderName.")
            attackedParts.forEach { attackedPart ->
                processAttackHit(event, attackedPart, subject, verb, defenderName, damageSource, defender, offensiveDamage)
            }
        }
    }

    private fun getAttackedParts(source: Target, sourcePart: BodyPart, target: TargetAim): List<BodyPart> {
        val sourcePosition = source.getPositionInLocation(sourcePart)
        val range = getRange(source, sourcePart)
        return target.bodyPartTargets.filter {
            val targetPartPosition = target.target.getPositionInLocation(it)
            val distance = sourcePosition.getDistance(targetPartPosition)
            range >= distance
        }
    }

    private fun getRange(source: Target, sourcePart: BodyPart) : Int {
        val weaponRange = sourcePart.getEquippedWeapon()?.properties?.getRange() ?: Distances.MIN_RANGE
        val bodyRange = source.body.getRange()
        return weaponRange + bodyRange
    }

    private fun processAttackHit(event: AttackEvent, attackedPart: BodyPart, subject: String, verb: String, defenderName: String, damageSource: String, defender: Combatant, offensiveDamage: Int) {
        val possessive = StringFormatter.getSubjectPossessive(event.source)
        display("$subject $verb the ${attackedPart.name} of $defenderName with $possessive $damageSource.")
        EventManager.postEvent(TakeDamageEvent(defender.target, attackedPart, offensiveDamage, event.type, damageSource))
    }

    private fun getOffensiveDamage(sourceCreature: Target, sourcePart: BodyPart, type: DamageType): Int {
        return when {
            sourcePart.getEquippedWeapon() != null -> sourcePart.getEquippedWeapon()!!.properties.values.getInt(type.damage, 0)
            else -> sourceCreature.soul.getCurrent(BARE_HANDED)
        }
    }


}