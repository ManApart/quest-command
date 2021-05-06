package combat.takeDamage

import combat.DamageType
import core.events.EventListener
import core.events.EventManager
import core.target.Target
import status.stat.HEALTH
import status.statChanged.StatChangeEvent
import traveling.location.location.Location
import kotlin.math.max

class TakeDamage : EventListener<TakeDamageEvent>() {
    override fun execute(event: TakeDamageEvent) {
        val undefendedDamage = getUndefendedDamage(event.source, event.damage, event.sourcePart, event.attackType)

        if (hasSpecificHealth(event.source, event.attackType)) {
            EventManager.postEvent(StatChangeEvent(event.source, event.damageSource, event.attackType.health, -undefendedDamage))
        } else if (event.source.soul.hasStat(HEALTH)) {
            EventManager.postEvent(StatChangeEvent(event.source, event.damageSource, HEALTH, -undefendedDamage))
        }
    }

    private fun getUndefendedDamage(source: Target, damage: Int, attackedPart: Location, attackType: DamageType): Int {
        var damageDefended = source.soul.getStatOrNull(attackType.defense)?.current ?: 0
        attackedPart.getEquippedItems().forEach {
            damageDefended += it.properties.getDefense(attackType.defense)
        }
        return max(damage-damageDefended, 0)
    }

    private fun hasSpecificHealth(target: Target, attackType: DamageType): Boolean {
        return target.soul.hasStat(attackType.health)
    }

}