package combat.takeDamage

import combat.AttackType
import core.events.EventListener
import core.gameState.Creature
import core.gameState.body.BodyPart
import core.gameState.stat.HEALTH
import status.statChanged.StatChangeEvent
import system.EventManager

class TakeDamage : EventListener<TakeDamageEvent>() {
    override fun execute(event: TakeDamageEvent) {
        val undefendedDamage = getUndefendedDamage((event.damage * event.hitLevel.modifier).toInt(), event.sourcePart, event.attackType)

        if (hasSpecificHealth(event.source, event.attackType)) {
            EventManager.postEvent(StatChangeEvent(event.source, event.damageSource, event.attackType.health, -undefendedDamage))
        } else if (event.source.soul.hasStat(HEALTH)) {
            EventManager.postEvent(StatChangeEvent(event.source, event.damageSource, HEALTH, -undefendedDamage))
        }
    }

    private fun getUndefendedDamage(damage: Int, attackedPart: BodyPart, attackType: AttackType): Int {
        var damageDefended = 0
        attackedPart.getEquippedItems().forEach {
            damageDefended += it.getDefense(attackType.defense)
        }
        return Math.max(damage-damageDefended, 0)
    }

    private fun hasSpecificHealth(target: Creature, attackType: AttackType): Boolean {
        return target.soul.hasStat(attackType.health)
    }

}