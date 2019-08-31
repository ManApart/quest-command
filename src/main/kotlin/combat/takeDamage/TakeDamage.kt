package combat.takeDamage

import combat.DamageType
import core.events.EventListener
import core.gameState.Target
import core.gameState.body.BodyPart
import core.gameState.stat.HEALTH
import status.statChanged.StatChangeEvent
import system.EventManager
import kotlin.math.max

class TakeDamage : EventListener<TakeDamageEvent>() {
    override fun execute(event: TakeDamageEvent) {
        val undefendedDamage = getUndefendedDamage(event.damage, event.sourcePart, event.attackType)

        if (hasSpecificHealth(event.source, event.attackType)) {
            EventManager.postEvent(StatChangeEvent(event.source, event.damageSource, event.attackType.health, -undefendedDamage))
        } else if (event.source.soul.hasStat(HEALTH)) {
            EventManager.postEvent(StatChangeEvent(event.source, event.damageSource, HEALTH, -undefendedDamage))
        }
    }

    private fun getUndefendedDamage(damage: Int, attackedPart: BodyPart, attackType: DamageType): Int {
        var damageDefended = 0
        attackedPart.getEquippedItems().forEach {
            damageDefended += it.properties.getDefense(attackType.defense)
        }
        return max(damage-damageDefended, 0)
    }

    private fun hasSpecificHealth(target: Target, attackType: DamageType): Boolean {
        return target.soul.hasStat(attackType.health)
    }

}