package combat

import combat.battle.position.HitLevel
import combat.battle.position.TargetPosition
import combat.takeDamage.TakeDamageEvent
import core.events.Event
import core.gameState.*
import core.gameState.Target
import core.gameState.body.Body
import core.gameState.body.BodyPart
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

        val defender = target.getCreature()
        val offensiveDamage = getOffensiveDamage(source, sourcePart, type)

        if (defender != null && offensiveDamage > 0) {
            val attackedPart = getAttackedPart(targetPosition, defender.body)
            if (attackedPart == null) {
                display("$subject ${StringFormatter.format(source.isPlayer(), "miss", "misses")}!")
            } else {
                val damageSource = sourcePart.getEquippedWeapon()?.name ?: sourcePart.name
                val possessive = StringFormatter.getSubjectPossessive(source)
                val verb = StringFormatter.format(source.isPlayer(), type.name.toLowerCase(), type.verb)
                val hitLevel = targetPosition.getHitLevel(attackedPart.position)
                val hitLevelString = StringFormatter.format(hitLevel == HitLevel.DIRECT, "directly", "grazingly")
                display("$subject $hitLevelString $verb the ${attackedPart.name} of ${target.name} with $possessive $damageSource.")

                EventManager.postEvent(TakeDamageEvent(defender, attackedPart, offensiveDamage,  hitLevel, type, damageSource))
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


}