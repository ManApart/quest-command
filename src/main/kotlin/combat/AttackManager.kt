package combat

import combat.battle.position.TargetPosition
import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Target
import core.gameState.body.Body
import core.gameState.body.BodyPart
import core.gameState.getCreature
import core.gameState.stat.Stat
import core.history.display
import core.utility.StringFormatter
import core.utility.random
import interact.UseEvent
import status.statChanged.StatChangeEvent
import system.EventManager

object AttackManager {

    fun execute(type: AttackType, source: Creature, sourcePart: BodyPart, target: Target, targetPosition: TargetPosition, event: Event) {
        val subject = StringFormatter.getSubject(source)
        val possessive = StringFormatter.getSubjectPossessive(source)
        display("$subject ${type.name.toLowerCase()} the $targetPosition of ${target.name} with $possessive ${sourcePart.getEquippedWeapon()}.")

        val defender = target.getCreature()
        val offensiveDamage = getOffensiveDamage(source, sourcePart, type)

        if (defender != null && offensiveDamage > 0) {
            val attackedPart = getAttackedPart(targetPosition, defender.body)
            if (attackedPart == null) {
                display("$subject misses!")
            } else {
                val grazeModifier = getGrazeModifier(targetPosition, attackedPart)
                val undefendedDamage = getUndefendedDamage((offensiveDamage*grazeModifier).toInt(), attackedPart, type)

                if (hasSpecificHealth(defender, type)) {
                    EventManager.postEvent(StatChangeEvent(defender, sourcePart.getEquippedWeapon()?.name
                            ?: "", type.health, -undefendedDamage))
                } else if (defender.soul.hasStat(Stat.HEALTH)) {
                    EventManager.postEvent(StatChangeEvent(defender, sourcePart.getEquippedWeapon()?.name
                            ?: "", Stat.HEALTH, -undefendedDamage))
                }
            }
        } else if (sourcePart.getEquippedWeapon() != null) {
            EventManager.postEvent(UseEvent(GameState.player.creature, sourcePart.getEquippedWeapon()!!, target))
        } else {
            display("Nothing happens.")
        }
        target.consume(event)
    }

    private fun getAttackedPart(targetPosition: TargetPosition, defender: Body): BodyPart? {
        var parts = defender.getDirectParts(targetPosition)
        if (parts.isEmpty()) {
            parts = defender.getGrazedParts(targetPosition)
        }
        return parts.random()
    }

    private fun getOffensiveDamage(sourceCreature: Creature, sourcePart: BodyPart, type: AttackType): Int {
        return when {
            sourcePart.getEquippedWeapon() != null -> sourcePart.getEquippedWeapon()!!.properties.values.getInt(type.damage, 0)
            else -> sourceCreature.soul.getCurrent(Stat.BARE_HANDED)
        }
    }

    private fun getGrazeModifier(targetPosition: TargetPosition, attackedPart: BodyPart): Float {
        return targetPosition.getHitLevel(attackedPart.position).modifier
    }

    private fun getUndefendedDamage(damage: Int, attackedPart: BodyPart, attackType: AttackType): Int {
        var defendedDamage = damage
        attackedPart.getEquippedItems().forEach {
            defendedDamage -= it.getDefense(attackType.defense)
        }
        return Math.max(defendedDamage, 0)
    }


    private fun hasSpecificHealth(target: Creature, attackType: AttackType): Boolean {
        return target.soul.hasStat(attackType.health)
    }


}