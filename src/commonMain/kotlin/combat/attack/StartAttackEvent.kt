package combat.attack

import combat.DamageType
import core.events.DelayedEvent
import core.events.Event
import core.thing.Thing
import status.stat.AGILITY
import traveling.location.location.Location
import traveling.position.ThingAim
import kotlin.math.max

class StartAttackEvent(override val source: Thing, private val sourcePart: Location, val thing: ThingAim, val type: DamageType, timeLeft: Int = -1) : Event, DelayedEvent {
    override var timeLeft = calcTimeLeft(timeLeft)

    private fun calcTimeLeft(defaultTimeLeft: Int): Int {
        return if (defaultTimeLeft != -1){
            defaultTimeLeft
        } else {
            val weaponSize = sourcePart.getEquippedWeapon()?.properties?.getRange() ?: 1
            val weaponWeight = sourcePart.getEquippedWeapon()?.getWeight() ?: 1
            val encumbrance = source.getEncumbrance()
            val agility = max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())
            val weaponScaleFactor = 10

            max(1, weaponSize * weaponWeight * weaponScaleFactor / agility)
        }
    }

    override fun getActionEvent(): AttackEvent {
        return AttackEvent(source, sourcePart, thing, type)
    }
}
